package cz.drekorian.avonmobilefetcher.http.catalog

import cz.drekorian.avonmobilefetcher.I18n
import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.logger
import khttp.responses.Response
import mu.KotlinLogging

class CatalogRequest : Request() {

    companion object {
        private const val URL = "$BASE_URL/%s/common/res/products_details.json"
    }

    /**
     * Sends the request. Attempts to load product data about a catalog with given [catalogId].
     *
     * @param catalogId unique catalog ID
     * @return valid [CatalogResponse] instance, provided that the request has finished successfully, null otherwise
     */
    fun send(catalogId: String): CatalogResponse? {
        val response: Response = KHttpClient.get(URL.format(catalogId))

        if (!checkStatusCode(response, logger, I18n.get("catalog_request_error").format(catalogId))) {
            return null
        }

        return CatalogResponse(response.jsonArray)
    }
}
