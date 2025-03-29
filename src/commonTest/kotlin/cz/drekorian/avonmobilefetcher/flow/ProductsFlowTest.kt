package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.CommonTest
import cz.drekorian.avonmobilefetcher.http.pagedata.PageDataRequest
import cz.drekorian.avonmobilefetcher.http.pagedata.PageDataResponse
import cz.drekorian.avonmobilefetcher.http.products.ProductsRequest
import cz.drekorian.avonmobilefetcher.http.products.ProductsResponse
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verify.VerifyMode.Companion.not
import dev.mokkery.verifySuspend
import kotlin.test.Test
import kotlin.test.assertEquals

private val campaign = Campaign("2025", "03")
private const val catalogId = "focus"

internal class ProductsFlowTest : CommonTest() {

    private val pageDataRequest: PageDataRequest = mock {
        everySuspend { send(campaign, Catalog(catalogId), any()) } calls { call ->
            val thirdArg = call.arg<Int>(2)
            mock<PageDataResponse> {
                every { pageData } returns mock {
                    every { page } returns thirdArg
                    every { ids } returns listOf("${thirdArg}0002")
                }
            }
        }
    }
    private val productsRequest: ProductsRequest = mock {
        everySuspend { send(campaign, catalogId) } returns ProductsResponse(
            products = listOf(
                mock<Product> {
                    every { id } returns "10001"
                    every { physicalPage } returns 1
                },
                mock<Product> {
                    every { id } returns "20001"
                    every { physicalPage } returns 2
                },
                mock<Product> {
                    every { id } returns "99999"
                    every { physicalPage } returns null
                },
            )
        )
    }

    private fun productsFlow(
        pageDataRequest: PageDataRequest = this.pageDataRequest,
        productsRequest: ProductsRequest = this.productsRequest,
    ): ProductsFlow = ProductsFlow(
        pageDataRequest = pageDataRequest,
        productsRequest = productsRequest,
    )

    @Test
    fun `when fetchProducts is called it returns the list of products`() {
        // when
        val result = productsFlow().fetchProducts(campaign = campaign, catalog = Catalog(catalogId))

        assertEquals(
            listOf("10001", "20001", "99999", "10002", "20002"),
            result.map { it.id },
        )

        // then
        verifySuspend(exactly(1)) { pageDataRequest.send(campaign, Catalog(catalogId), 1) }
        verifySuspend(exactly(1)) { pageDataRequest.send(campaign, Catalog(catalogId), 2) }
    }

    @Test
    fun `when products request fails it returns an empty list`() {
        // given
        everySuspend { productsRequest.send(any(), any()) } returns null

        // when
        val result = productsFlow().fetchProducts(campaign = campaign, catalog = Catalog(catalogId))

        // then
        assertEquals(
            emptyList(),
            result,
        )
        verifySuspend(not) { pageDataRequest.send(any(), any(), any()) }
    }

    @Test
    fun `when product request hits no results it returns an empty list`() {
        // given
        everySuspend { productsRequest.send(any(), any()) } returns ProductsResponse(emptyList())

        // when
        val result = productsFlow().fetchProducts(campaign = campaign, catalog = Catalog(catalogId))

        // then
        assertEquals(
            emptyList(),
            result,
        )
        verifySuspend(not) { pageDataRequest.send(any(), any(), any()) }
    }

    @Test
    fun `when page data request fails it returns an products without page data`() {
        // given
        everySuspend { pageDataRequest.send(any(), any(), any()) } returns null

        // when
        val result = productsFlow().fetchProducts(campaign = campaign, catalog = Catalog(catalogId))

        // then
        assertEquals(
            listOf("10001", "20001", "99999"),
            result.map { it.id },
        )

        assertEquals(
            listOf(1, 2, null),
            result.map { it.physicalPage },
        )
    }

    @Test
    fun `when page data request contains an invalid id it is not added to the result`() {
        // given
        everySuspend { pageDataRequest.send(campaign, Catalog(catalogId), any()) } calls { call ->
            val thirdArg = call.arg<Int>(2)
            mock<PageDataResponse> {
                every { pageData } returns mock {
                    every { page } returns thirdArg
                    every { ids } returns listOf("99999")
                }
            }
        }

        // when
        val result = productsFlow().fetchProducts(campaign = campaign, catalog = Catalog(catalogId))

        // then
        assertEquals(
            listOf("10001", "20001", "99999"),
            result.map { it.id },
        )

        assertEquals(
            listOf(1, 2, null),
            result.map { it.physicalPage },
        )
    }

    @Test
    fun `when page data request contains an already detected product id it is not added to the result`() {
        // given
        everySuspend { pageDataRequest.send(campaign, Catalog(catalogId), any()) } calls { call ->
            val thirdArg = call.arg<Int>(2)
            mock<PageDataResponse> {
                every { pageData } returns mock {
                    every { page } returns thirdArg
                    every { ids } returns listOf("${thirdArg}0001")
                }
            }
        }

        // when
        val result = productsFlow().fetchProducts(campaign = campaign, catalog = Catalog(catalogId))

        // then
        assertEquals(
            listOf("10001", "20001", "99999"),
            result.map { it.id },
        )

        assertEquals(
            listOf(1, 2, null),
            result.map { it.physicalPage },
        )
    }

    @Test
    fun `when invalid id is both in products and page data it is not added to the result`() {
        // given
        everySuspend { pageDataRequest.send(campaign, Catalog(catalogId), any()) } calls { call ->
            val thirdArg = call.arg<Int>(2)
            mock<PageDataResponse> {
                every { pageData } returns mock {
                    every { page } returns thirdArg
                    every { ids } returns listOf("00000")
                }
            }
        }
        everySuspend { productsRequest.send(campaign, catalogId) } returns ProductsResponse(
            products = listOf(
                mock<Product> {
                    every { id } returns "00000"
                    every { physicalPage } returns 1
                },
            )
        )

        // when
        val result = productsFlow().fetchProducts(campaign = campaign, catalog = Catalog(catalogId))

        // then
        assertEquals(
            listOf("00000"),
            result.map { it.id },
        )

        assertEquals(
            listOf(1),
            result.map { it.physicalPage },
        )
    }
}
