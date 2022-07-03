package cz.drekorian.avonmobilefetcher.http.pagedata

import cz.drekorian.avonmobilefetcher.model.PageData

/**
 * This object constructs the PageData from raw XML.
 *
 * @author Marek Osvald
 */
object PageDataFactory {

    private val ID_REGEX = "<Text>(\\d{5})</Text>".toRegex()

    /**
     * Creates [PageData] instance from given raw [xml].
     *
     * @param page page number
     * @param xml raw XML data
     */
    fun createPageData(page: Int, xml: String): PageData {
        val ids = ID_REGEX
            .findAll(xml)
            .mapNotNull { it.groups[1]?.value }
            .toList()

        return PageData(page, ids)
    }
}
