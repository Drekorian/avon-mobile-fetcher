package cz.drekorian.avonmobilefetcher.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

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
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Product constructor(
    @JsonNames("sku") val id: String,
    val title: String,
    val category: String? = "",
    @Suppress("SpellCheckingInspection")
    @JsonNames("fizical_page")
    val physicalPage: Int = 0,
    @JsonNames("display_page") val displayPage: String = "",
) {

    companion object {

        /**
         * Constructs a new [Product] instance from given [id] and [page].
         */
        fun fromPageData(id: String, page: Int): Product = Product(id, page)
    }

    private constructor(id: String, page: Int) : this(
        id = id,
        title = "",
        physicalPage = page,
        displayPage = "$page",
    )
}
