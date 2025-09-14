package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.OpenForTesting

/**
 * This data class holds the product IDs on a single catalog page.
 *
 * @property page page number
 * @property ids unique IDs found within page data
 * @author Marek Osvald
 */
@OpenForTesting
internal data class PageData(val page: Int, val ids: Collection<String>)
