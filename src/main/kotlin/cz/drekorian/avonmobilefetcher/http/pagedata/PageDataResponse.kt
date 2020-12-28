package cz.drekorian.avonmobilefetcher.http.pagedata

import cz.drekorian.avonmobilefetcher.model.PageData

/**
 * This class holds the [PageData] for a single catalog page.
 *
 * @property pageData stored page data
 * @see PageData
 * @see PageDataFactory
 * @see PageDataRequest
 * @author Marek Osvald
 */
class PageDataResponse private constructor(val pageData: PageData) {

    companion object {

        /**
         * Creates [PageDataResponse] using [PageDataFactory].
         */
        fun fromXml(page: Int, xml: String): PageDataResponse =
            PageDataResponse(PageDataFactory.createPageData(page, xml))
    }
}
