package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.i18n
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product

/**
 * This request attempt to load product details for given product.
 *
 * @see ProductDetailsResponse
 * @author Marek Osvald
 */
class ProductDetailsRequest : Request() {

    companion object {
        private const val URL = "$BASE_URL/%s/common/products/xml/%s.xml"
    }

    /**
     * Sends the request. Attempts to load data product details for given [catalog] and [product].
     *
     * @return valid [ProductDetailsResponse] instance, provided that the request has finished successfully, null
     * otherwise
     */
    fun send(catalog: Catalog, product: Product): ProductDetailsResponse? {
        val response = KHttpClient.get(URL.format(catalog.id, product.id))

        if (!checkStatusCode(response, i18n("product_details_request_error"))) {
            return null
        }

        return ProductDetailsResponse.fromXml(response.text, catalog.id)
    }
}
