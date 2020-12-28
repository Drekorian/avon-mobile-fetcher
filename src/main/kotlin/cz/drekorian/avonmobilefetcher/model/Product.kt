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
class Product {

    companion object {

        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_CATEGORY = "category"
        @Suppress("SpellCheckingInspection") // Sic! Yes, really.
        const val KEY_PHYSICAL_PAGE = "fizical_page"
        const val KEY_DISPLAY_PAGE = "display_page"

        /**
         * Constructs a new [Product] instance from given [id] and [page].
         */
        fun fromPageData(id: String, page: Int): Product = Product(id, page)
    }

    /**
     * Constructs a new [Product] instance from given JSON [data].
     */
    constructor(data: JSONObject) {
        id = data.optString(KEY_ID)
        title = data.optString(KEY_TITLE)
        category = data.optInt(KEY_CATEGORY)
        physicalPage = data.optInt(KEY_PHYSICAL_PAGE)
        displayPage = data.optInt(KEY_DISPLAY_PAGE)
    }

    private constructor(id: String, page: Int) {
        this.id = id
        this.title = ""
        this.category = 0
        this.physicalPage = page
        this.displayPage = page
    }

    val id: String
    val title: String
    val category: Int
    val physicalPage: Int
    val displayPage: Int
}
