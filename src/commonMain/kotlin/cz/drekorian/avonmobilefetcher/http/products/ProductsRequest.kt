package cz.drekorian.avonmobilefetcher.http.products

import cz.drekorian.avonmobilefetcher.nFormat
import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.resources.i18n
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * This request attempts to load a list of products for a given catalog.
 *
 * @see ProductsResponse
 * @author Marek Osvald
 */
class ProductsRequest : Request() {

    companion object {
        private const val URL = "$BASE_URL/%s/%s/common/feed/publication_products.json"
    }

    /**
     * Sends the request. Attempts to load product data about a catalog with given [campaign] and [catalogId].
     *
     * @param campaign current campaign
     * @param catalogId unique catalog ID
     * @return valid [ProductsResponse] instance, provided that the request has finished successfully, null otherwise
     */
    suspend fun send(campaign: Campaign, catalogId: String): ProductsResponse? {
        val response: HttpResponse = KtorHttpClient.get(URL.nFormat(campaign.toRestfulArgument(), catalogId))

        if (!checkStatusCode(response, i18n("products_request_error").nFormat(catalogId))) {
            return null
        }

        return ProductsResponse(response.body())
    }
}
