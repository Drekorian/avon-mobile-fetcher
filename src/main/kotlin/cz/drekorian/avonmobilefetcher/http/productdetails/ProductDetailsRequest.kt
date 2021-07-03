package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.i18n
import cz.drekorian.avonmobilefetcher.model.Campaign
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
        private const val URL = "$BASE_URL/%s/%s/common/products/xml/%s.xml"
    }

    /**
     * Sends the request. Attempts to load data product details for given [campaign], [catalog] and [product].
     *
     * @param campaign current campaign
     * @param catalog selected catalog
     * @param product given catalog product
     * @return valid [ProductDetailsResponse] instance, provided that the request has finished successfully, null
     * otherwise
     */
    fun send(campaign: Campaign, catalog: Catalog, product: Product): ProductDetailsResponse? {
        val response = KHttpClient.get(URL.format(campaign.toRestfulArgument(), catalog.id, product.id))

        if (!checkStatusCode(response, i18n("product_details_request_error"))) {
            return null
        }

        return ProductDetailsResponse.fromXml(campaign, response.text, catalog.id)
    }
}
