package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.CommonTest
import cz.drekorian.avonmobilefetcher.domain.RuntimeOverride
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsFlow
import cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsRequest
import cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsResponse
import cz.drekorian.avonmobilefetcher.model.Catalog
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verify.VerifyMode.Companion.not
import dev.mokkery.verifySuspend
import kotlin.test.Test
import kotlin.test.assertEquals

private const val rawHtml = """onclick="open_catalog({url: 'main/'"""

internal class CatalogsFlowTest : CommonTest() {

    private val catalogsRequest: CatalogsRequest = mock {
        everySuspend { send() } returns CatalogsResponse(rawHtml)
    }

    private val runtimeOverride: RuntimeOverride = mock {
        every { this@mock.catalogs } returns emptyList()
    }

    private fun catalogsFlow(
        catalogsRequest: CatalogsRequest = this.catalogsRequest,
        runtimeOverride: RuntimeOverride = this.runtimeOverride,
    ): CatalogsFlow = CatalogsFlow(
        catalogsRequest = catalogsRequest,
        runtimeOverride = runtimeOverride,
    )

    @Test
    fun `when override is not set it should fetch catalogs`() {
        val result = catalogsFlow().fetchCatalogs()

        assertEquals(
            listOf("main", "focus").map(::Catalog),
            result,
        )
        verifySuspend(exactly(1)) { catalogsRequest.send() }
    }

    @Test
    fun `when fetched catalogs are null it should return a list with focus`() {
        everySuspend { catalogsRequest.send() } returns null

        val result = catalogsFlow().fetchCatalogs()

        assertEquals(
            listOf(Catalog.FOCUS),
            result,
        )
    }

    @Test
    fun `when raw html does not contain any catalogs it should return an empty list`() {
        everySuspend { catalogsRequest.send() } returns CatalogsResponse("")

        val result = catalogsFlow().fetchCatalogs() - Catalog.FOCUS

        assertEquals(
            emptyList(),
            result,
        )
    }

    @Test
    fun `when catalogs are fetched from the signpost only distinct results are returned`() {
        val rawHtml = """
            onclick="open_catalog({url: 'first/'
            onclick="open_catalog({url: 'second/'
            onclick="open_catalog({url: 'first/'
        """
        everySuspend { catalogsRequest.send() } returns CatalogsResponse(rawHtml)

        val result = catalogsFlow().fetchCatalogs() - Catalog.FOCUS

        assertEquals(
            listOf("first", "second").map(::Catalog),
            result,
        )
    }

    @Test
    fun `when override is set the catalogs from override should be used`() {
        val catalogs = listOf("first", "second")
        every { runtimeOverride.catalogs } returns catalogs

        val result = catalogsFlow().fetchCatalogs()

        assertEquals(
            catalogs.map(::Catalog),
            result,
        )
        verifySuspend(not) { catalogsRequest.send() }
    }
}
