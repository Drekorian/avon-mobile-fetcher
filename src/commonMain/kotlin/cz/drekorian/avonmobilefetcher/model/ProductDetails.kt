package cz.drekorian.avonmobilefetcher.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * This data class stores product details.
 *
 * @property category human-readable category of the product
 * @property description localized description of the product
 * @property id unique product identifier
 * @property priceStandard localized normal price (without any sales)
 * @property title name of the product
 * @property price localized current price
 * @property unitMeasure product measurement units (e.g., "g", "ml")
 * @property unitNumber product measurement volume
 * @property variant a variant of the product in case there is one
 * @property displayPage location in the digital brochure
 * @property physicalPage location in the printed brochure
 * @property sku unique stock-taking unit
 * @property images a list of image of the product
 * @property shadeFile shade image file for products with multiple shades
 * @author Marek Osvald
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ProductDetails(
    val category: String,
    val description: String,
    @JsonNames("sku") val id: String,
    @JsonNames("price_standard") val priceStandard: String,
    val title: String,
    val price: String,
    @JsonNames("unit_measure") val unitMeasure: String,
    @JsonNames("unit_number") val unitNumber: String,
    val variant: String,
    @JsonNames("display_page") val displayPage: String = "",
    @Suppress("SpellCheckingInspection")
    @JsonNames("fizical_page") private val _physicalPage: String? = null,
    @JsonNames("image_file") private val _imageFile: String = "",
    @JsonNames("images") private val _images: List<String> = emptyList(),
    @JsonNames("shade_file") private val _shadeFile: String = "",
) {
    companion object {

        private const val IMAGE_BASE_URL = "https://media.ce.avon.digital-catalogue.com"
    }

    // TODO (marek.osvald): duplicate in order to keep the existing format, subject to be removed
    val sku: String
        get() = id

    val physicalPage: Int
        get() = _physicalPage?.toIntOrNull() ?: 0

    val images: List<String>
        get() = buildList {
            if (_images.isNotEmpty()) {
                add(_imageFile)
            }
            addAll(_images)
        }.distinct().map { image -> "$IMAGE_BASE_URL/$image" }

    val shadeFile: String
        get() = _shadeFile.takeIf { shadeFile -> shadeFile.isNotEmpty() }
            ?.let { shadeFile -> "$IMAGE_BASE_URL/$shadeFile" }
            ?: ""
}
