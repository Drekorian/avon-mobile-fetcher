package cz.drekorian.avonmobilefetcher.productsapi

data class Product(
    val sku: String,
    val conceptCode: String,
    val fsc: String,
    val title: String,
    val description: String,
    val price: Double,
    val priceStandard: Double,
    val productOrder: String,
    val hasPerfectCorp: String,
    val hasPerfectCorpModel: String,
    val perfectCorpPatterns: String,
    val perfectCorpPatternsText: String,
) {

    companion object {

        internal fun fromDao(dao: ProductDao): Product = with(dao) {
            Product(
                sku = sku,
                conceptCode = conceptCode ?: "",
                fsc = fsc,
                title = title,
                description = description,
                price = price,
                priceStandard = priceStandard ?: 0.0,
                productOrder = productOrder ?: "",
                hasPerfectCorp = hasPerfectCorp ?: "",
                hasPerfectCorpModel = hasPerfectCorpModel ?: "",
                perfectCorpPatterns = perfectCorpPatterns ?: "",
                perfectCorpPatternsText = perfectCorpPatternsText ?: "",
            )
        }
    }
}
