package cz.drekorian.avonmobilefetcher.http.catalogs

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.i18n
import khttp.responses.Response

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
    fun send(): CatalogsResponse? {
        val response: Response = KHttpClient.get(url = URL)

        if (!checkStatusCode(response, i18n("catalogs_request_error"))) {
            return null
        }

        return CatalogsResponse(response.text)
    }
}
