package cz.drekorian.avonmobilefetcher.http.catalogs

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.resources.i18n
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * This request fetches the data from [BASE_URL] (mobile catalog signpost).
 *
 * @see CatalogsResponse
 * @author Marek Osvald
 */
class CatalogsRequest : Request() {

    companion object {
        private const val URL = BASE_URL
    }

    /**
     * Sends the request. Attempts to load data about available catalogs.
     *
     * @return valid [CatalogsResponse] instance, provided that the request has finished successfully, null otherwise
     */
    suspend fun send(): CatalogsResponse? {
        val response: HttpResponse = KtorHttpClient.get(url = URL)

        if (!checkStatusCode(response, i18n("catalogs_request_error"))) {
            return null
        }

        return CatalogsResponse(response.body())
    }
}
