package cz.drekorian.avonmobilefetcher.http.products

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.i18n
import cz.drekorian.avonmobilefetcher.model.Campaign
import khttp.responses.Response

/**
 * This request attempts to load a list of products for a given catalog.
 *
 * @see ProductsResponse
 * @author Marek Osvald
 */
class ProductsRequest : Request() {

    companion object {
        private const val URL = "$BASE_URL/%s/%s/common/res/products_details.json"
    }

    /**
     * Sends the request. Attempts to load product data about a catalog with given [campaign] and [catalogId].
     *
     * @param campaign current campaign
     * @param catalogId unique catalog ID
     * @return valid [ProductsResponse] instance, provided that the request has finished successfully, null otherwise
     */
    fun send(campaign: Campaign, catalogId: String): ProductsResponse? {
        val response: Response = KHttpClient.get(URL.format(campaign.toRestfulArgument(), catalogId))

        if (!checkStatusCode(response, i18n("products_request_error").format(catalogId))) {
            return null
        }

        return ProductsResponse(response.jsonArray)
    }
}
