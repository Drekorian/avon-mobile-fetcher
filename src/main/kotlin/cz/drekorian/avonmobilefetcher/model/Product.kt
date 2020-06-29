package cz.drekorian.avonmobilefetcher.model

import org.json.JSONObject

/**
 * This data class holds data about catalog products
 *
 * @property id unique product ID
 * @property title product title
 * @property category product category
 * @property physicalPage product page position in physical catalog
 * @property displayPage product position in mobile catalog
 * @author Marek Osvald
 */
class Product(data: JSONObject) {

    companion object {

        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_CATEGORY = "category"
        @Suppress("SpellCheckingInspection") // Sic! Yes, really.
        const val KEY_PHYSICAL_PAGE = "fizical_page"
        const val KEY_DISPLAY_PAGE = "display_page"
    }

    val id: String = data.optString(KEY_ID)

    val title: String = data.optString(KEY_TITLE)

    val category: Int = data.optInt(KEY_CATEGORY)

    val physicalPage: Int = data.optInt(KEY_PHYSICAL_PAGE)

    val displayPage: Int = data.optInt(KEY_DISPLAY_PAGE)
}
