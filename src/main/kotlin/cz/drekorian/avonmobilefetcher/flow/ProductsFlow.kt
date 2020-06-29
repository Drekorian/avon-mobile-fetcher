package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.errorI18n
import cz.drekorian.avonmobilefetcher.http.products.ProductsRequest
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product

/**
 * This flow fetches the list of products for given catalog.
 *
 * @see ProductsRequest
 * @author Marek Osvald
 */
class ProductsFlow {

    /**
     * Fetches the list of [Product]s for given [catalog].
     *
     * @param catalog for which the product should be fetched
     * @return the list of [Product]s for given [catalog]
     */
    fun fetchProducts(catalog: Catalog): List<Product> {

        val response = ProductsRequest().send(catalog.id)
        if (response == null) {
            logger.errorI18n("products_response_null", catalog.id)
            return emptyList()
        }

        return response.products
    }
}
