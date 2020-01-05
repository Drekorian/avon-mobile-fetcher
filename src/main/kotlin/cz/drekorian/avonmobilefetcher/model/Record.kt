package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.CSV_SEPARATOR

class Record(
    private val catalog: Catalog,
    private val product: Product,
    private val productDetails: ProductDetails
) {

    companion object {
        private const val PRODUCT_ID_LENGTH = 5
        private const val PRODUCT_ID_PADDING = '0'
        private const val LINE_SEPARATOR = "____"
    }

    fun toCsv(campaign: Campaign): String =
        """
        |${campaign.year}
        |${campaign.id}
        |${catalog.name}
        |${product.category}
        |${productDetails.category}
        |${product.physicalPage}
        |${productDetails.physicalPage}
        |${product.displayPage}
        |${productDetails.displayPage}
        |="${product.id.padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PADDING)}"
        |="${productDetails.id.padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PADDING)}"
        |="${productDetails.sku.padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PADDING)}"
        |"${product.title
            .lines()
            .joinToString(separator = LINE_SEPARATOR)
        }"
        |"${productDetails.title
            .replace("\"", "\"\"")
            .lines()
            .joinToString(separator = LINE_SEPARATOR)
        }"
        |${productDetails.variant}
        |${productDetails.price}
        |${productDetails.priceStandard}
        |"${productDetails.description
            .replace("\"", "\"\"")
            .lines()
            .joinToString(separator = LINE_SEPARATOR)
        }"
        |"${productDetails.images.joinToString(separator = LINE_SEPARATOR) { it.url }
            .lines()
            .joinToString(separator = LINE_SEPARATOR)
        }"
        |${productDetails.shadeFile}
        |"""
            .lines()
            .filterIndexed { index, _ -> index != 0 }
            .joinToString(separator = CSV_SEPARATOR) { it.trimMargin("|") }
            .replace(LINE_SEPARATOR, "\n")
}
