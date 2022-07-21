package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.http.BASE_HOST
import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product
import cz.drekorian.avonmobilefetcher.resources.i18n
import io.ktor.client.call.body
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

/**
 * This request attempt to load product details for given product.
 *
 * @see ProductDetailsResponse
 * @author Marek Osvald
 */
class ProductDetailsRequest : Request() {

    /**
     * Sends the request. Attempts to load data product details for given [campaign], [catalog] and [product].
     *
     * @param campaign current campaign
     * @param catalog selected catalog
     * @param product given catalog product
     * @return valid [ProductDetailsResponse] instance, provided that the request has finished successfully, null
     * otherwise
     */
    suspend fun send(campaign: Campaign, catalog: Catalog, product: Product): ProductDetailsResponse? {
        val response = KtorHttpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_HOST
                appendPathSegments(
                    campaign.toRestfulArgument(),
                    catalog.id,
                    "common",
                    "feed",
                    "products",
                    "${product.id}.json"
                )
            }
        }

        if (!checkStatusCode(response, i18n("product_details_request_error"))) {
            return null
        }

        return ProductDetailsResponse(response.body())
    }
}
