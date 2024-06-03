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
 * @property _physicalPage product page position in physical catalog
 * @property displayPage product position in mobile catalog
 * @author Marek Osvald
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Product(
    @JsonNames("sku") private val _id: String,
    val title: String,
    val category: String? = "",
    @Suppress("SpellCheckingInspection")
    @JsonNames("fizical_page")
    private val _physicalPage: String = "",
    @JsonNames("display_page") val displayPage: String = "",
) {

    companion object {

        /**
         * Constructs a new [Product] instance from given [id] and [page].
         */
        fun fromPageData(id: String, page: Int): Product = Product(id, page)
    }

    private constructor(id: String, page: Int) : this(
        _id = id,
        title = "",
        _physicalPage = "$page",
        displayPage = "$page",
    )

    val id: String
        get() = _id.trimEnd()

    val physicalPage: Int?
        get() = _physicalPage.toIntOrNull()
}
