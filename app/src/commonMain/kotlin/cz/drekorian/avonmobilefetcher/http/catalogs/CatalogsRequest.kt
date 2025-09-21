package cz.drekorian.avonmobilefetcher.http.catalogs

import cz.drekorian.avonmobilefetcher.OpenForTesting
import cz.drekorian.avonmobilefetcher.http.BASE_HOST
import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Representative
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.resources.i18n
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

/**
 * This request fetches the data from [BASE_HOST] (mobile catalog signpost).
 *
 * @see CatalogsResponse
 * @author Marek Osvald
 */
@OpenForTesting
internal class CatalogsRequest : Request() {

    /**
     * Sends the request. Attempts to load data about available catalogs.
     *
     * @return valid [CatalogsResponse] instance, provided that the request has finished successfully, null otherwise
     */
    suspend fun send(): CatalogsResponse? {
        val response: HttpResponse = KtorHttpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_HOST
                appendPathSegments("avon", Representative)
            }
        }

        if (!checkStatusCode(response, i18n("catalogs_request_error"))) {
            return null
        }

        return CatalogsResponse(response.body())
    }
}
