package cz.drekorian.avonmobilefetcher.model

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
data class ProductDetails(
    val id: String,
    val images: List<Image>,
    val title: String,
    val description: String,
    val variant: String,
    val category: String,
    val sku: String,
    val price: String,
    val priceStandard: String,
    val physicalPage: Int,
    val displayPage: String,
    val shadeFile: String
)
