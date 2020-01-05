package cz.drekorian.avonmobilefetcher.model

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
    val displayPage: Int,
    val shadeFile: String
)