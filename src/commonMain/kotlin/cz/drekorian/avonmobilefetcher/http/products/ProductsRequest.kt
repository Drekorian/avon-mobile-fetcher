package cz.drekorian.avonmobilefetcher.http.products

import cz.drekorian.avonmobilefetcher.http.BASE_HOST
import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.nFormat
import cz.drekorian.avonmobilefetcher.resources.i18n
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

/**
 * This request attempts to load a list of products for a given catalog.
 *
 * @see ProductsResponse
 * @author Marek Osvald
 */
class ProductsRequest : Request() {

    /**
     * Sends the request. Attempts to load product data about a catalog with given [campaign] and [catalogId].
     *
     * @param campaign current campaign
     * @param catalogId unique catalog ID
     * @return valid [ProductsResponse] instance, provided that the request has finished successfully, null otherwise
     */
    suspend fun send(campaign: Campaign, catalogId: String): ProductsResponse? {
        val response: HttpResponse = KtorHttpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_HOST
                appendPathSegments(
                    campaign.toRestfulArgument(),
                    catalogId,
                    "common",
                    "feed",
                    "publication_products.json"
                )
            }
        }

        if (!checkStatusCode(response, i18n("products_request_error").nFormat(catalogId))) {
            return null
        }

        return ProductsResponse(response.body())
    }
}
