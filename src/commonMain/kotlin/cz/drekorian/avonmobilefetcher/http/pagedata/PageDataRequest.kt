package cz.drekorian.avonmobilefetcher.http.pagedata

import cz.drekorian.avonmobilefetcher.http.BASE_HOST
import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.nFormat
import cz.drekorian.avonmobilefetcher.resources.i18n
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

/**
 * This request attempt to load the list of products for given page of a given catalog.
 *
 * @see PageDataResponse
 * @author Marek Osvald
 */
class PageDataRequest : Request() {

    companion object {

        private const val PAGE_DATA_PAGE_NUMBER_LENGTH = 4
        private const val PAGE_DATA_PAGE_PAD_START = '0'
    }

    /**
     * Sends the request. Attempts to load page data for given [campaign], [catalog] and [page].
     *
     * @param campaign current campaign
     * @param catalog selected Catalog data
     * @param page selected catalog page
     * @return valid [PageDataResponse] instance, provided that the request has finished successfully, null
     * otherwise
     */
    suspend fun send(campaign: Campaign, catalog: Catalog, page: Int): PageDataResponse? {
        val pageNumber = page.toString().padStart(PAGE_DATA_PAGE_NUMBER_LENGTH, PAGE_DATA_PAGE_PAD_START)
        val response: HttpResponse = KtorHttpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_HOST
                appendPathSegments(campaign.toRestfulArgument(), catalog.id, "common", "data", pageNumber)
            }
        }

        if (!checkStatusCode(response, i18n("page_data_request_error").nFormat(page.toString(), catalog.id))) {
            return null
        }

        return PageDataResponse.fromXml(page, response.body())
    }
}
