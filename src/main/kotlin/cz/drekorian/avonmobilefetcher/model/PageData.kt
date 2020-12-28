package cz.drekorian.avonmobilefetcher.model

/**
 * This data class holds the product IDs on a single catalog page.
 *
 * @property page page number
 * @property ids unique IDs found within page data
 * @author Marek Osvald
 */
data class PageData(val page: Int, val ids: Collection<String>)
