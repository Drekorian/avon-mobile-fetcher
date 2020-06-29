package cz.drekorian.avonmobilefetcher.http.products

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.i18n
import khttp.responses.Response

/**
 * This request attempts to load a list of products for a given catalog.
 *
 * @see ProductsResponse
 * @author Marek Osvald
 */
class ProductsRequest : Request() {

    companion object {
        private const val URL = "$BASE_URL/%s/common/res/products_details.json"
    }

    /**
     * Sends the request. Attempts to load product data about a catalog with given [catalogId].
     *
     * @param catalogId unique catalog ID
     * @return valid [ProductsResponse] instance, provided that the request has finished successfully, null otherwise
     */
    fun send(catalogId: String): ProductsResponse? {
        val response: Response = KHttpClient.get(URL.format(catalogId))

        if (!checkStatusCode(response, i18n("catalog_request_error").format(catalogId))) {
            return null
        }

        return ProductsResponse(response.jsonArray)
    }
}
