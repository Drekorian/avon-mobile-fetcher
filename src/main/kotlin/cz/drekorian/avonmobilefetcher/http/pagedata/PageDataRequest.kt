package cz.drekorian.avonmobilefetcher.http.pagedata

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KtorHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.i18n
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Catalog
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * This request attempt to load the list of products for given page of a given catalog.
 *
 * @see PageDataResponse
 * @author Marek Osvald
 */
class PageDataRequest : Request() {

    companion object {

        private const val URL = "$BASE_URL/%s/%s/common/data/%s.xml"
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
        val response: HttpResponse = KtorHttpClient.get(URL.format(campaign.toRestfulArgument(), catalog.id, pageNumber))

        if (!checkStatusCode(response, i18n("page_data_request_error").format(page, catalog))) {
            return null
        }

        return PageDataResponse.fromXml(page, response.body())
    }
}
