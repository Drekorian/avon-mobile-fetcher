package cz.drekorian.avonmobilefetcher.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * This data class stores product details.
 *
 * @property id unique product identifier
 * @property images a list of image of the product
 * @property title name of the product
 * @property description localized description of the product
 * @property variant a variant of the product in case there is one
 * @property category human-readable category of the product
 * @property sku unique stock-taking unit
 * @property price localized current price
 * @property priceStandard localized normal price (without any sales)
 * @property physicalPage location in the printed brochure
 * @property displayPage location in the digital brochure
 * @property shadeFile shade image file for products with multiple shades
 * @author Marek Osvald
 */
@Serializable
data class ProductDetails(
    val category: String,
    val description: String,
    @JsonNames("sku") val id: String,
    @JsonNames("price_standard") val priceStandard: String,
    val title: String,
    val price: String,
    val variant: String,
    @JsonNames("display_page") val displayPage: String = "",
    @JsonNames("fizical_page") val physicalPage: Int = 0,
    @JsonNames("image_file") private val _imageFile: String,
    @JsonNames("images") private val _images: List<String>,
    val shadeFile: String = "",
) {
    companion object {

        private const val IMAGE_BASE_URL = "https://media.ce.avon.digital-catalogue.com"
    }

    // TODO (marek.osvald): duplicate in order to keep the existing format, subject to be removed
    val sku: String
        get() = id

    val images: List<String>
        get() = (listOf(_imageFile) + _images).distinct().map { image -> "$IMAGE_BASE_URL/$image" }
}
