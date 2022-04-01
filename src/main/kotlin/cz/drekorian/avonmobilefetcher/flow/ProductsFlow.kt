package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.debugI18n
import cz.drekorian.avonmobilefetcher.errorI18n
import cz.drekorian.avonmobilefetcher.http.pagedata.PageDataRequest
import cz.drekorian.avonmobilefetcher.http.products.ProductsRequest
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product

/**
 * This flow fetches the list of products for given catalog.
 *
 * @see ProductsRequest
 * @author Marek Osvald
 */
class ProductsFlow {

    companion object {

        private const val INVALID_ID = "00000"
    }

    /**
     * Fetches the list of [Product]s for given [campaign] and [catalog].
     *
     * @param campaign current campaign
     * @param catalog for which the product should be fetched
     * @return the list of [Product]s for given [catalog]
     */
    fun fetchProducts(campaign: Campaign, catalog: Catalog): List<Product> {
        logger.infoI18n("products_request", catalog.name)
        val response = ProductsRequest().send(campaign, catalog.id)
        if (response == null) {
            logger.errorI18n("products_response_null", catalog.id)
            return emptyList()
        }

        val products = response.products.toMutableList()
        val maxPage = products.maxByOrNull { it.physicalPage }!!.physicalPage

        (1..maxPage).forEach { page ->
            logger.debugI18n("page_data_request", page, catalog.name)
            val pageResponse = PageDataRequest().send(campaign, catalog, page)
            if (pageResponse == null) {
                logger.errorI18n("page_data_response_null", page, catalog.name)
                return@forEach
            }

            val foundIds = products.map { product -> product.id }
            pageResponse.pageData.ids.forEach { id ->
                if (id != INVALID_ID && id !in foundIds) {
                    logger.debugI18n("page_data_new_product", page, catalog.name, id)
                    products += Product.fromPageData(id, page)
                }
            }
        }

        return products
    }
}
