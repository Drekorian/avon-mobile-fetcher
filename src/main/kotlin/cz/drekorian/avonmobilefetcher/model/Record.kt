package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.CSV_SEPARATOR

/**
 * This data class stores the combined info about product and product details.
 *
 * @property catalog product catalog
 * @property product product
 * @property productDetails product details
 * @author Marek Osvald
 */
data class Record(
    private val catalog: Catalog,
    private val product: Product,
    private val productDetails: ProductDetails?
) {

    companion object {
        private const val PRODUCT_ID_LENGTH = 5
        private const val PRODUCT_ID_PADDING = '0'
        private const val LINE_SEPARATOR = "____"
    }

    /**
     * Serializes the record into a CSV format.
     *
     * @param campaign current campaign
     * @return A record in a CSV format
     */
    fun toCsv(campaign: Campaign): String =
        """
        |${campaign.year}
        |${campaign.id}
        |${catalog.id}
        |${product.category}
        |${productDetails?.category ?: ""}
        |${product.physicalPage}
        |${productDetails?.physicalPage ?: 0}
        |${product.displayPage}
        |${productDetails?.displayPage ?: ""}
        |="${product.id.padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PADDING)}"
        |="${productDetails?.id?.padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PADDING) ?: ""}"
        |="${productDetails?.sku?.padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PADDING) ?: ""}"
        |"${product.title
            .replace(";", ",")
            .lines()
            .joinToString(separator = LINE_SEPARATOR)
        }"
        |"${productDetails?.title
            ?.replace("\"", "\"\"")
            ?.lines()
            ?.joinToString(separator = LINE_SEPARATOR)
            ?: ""
        }"
        |"${productDetails?.variant
            ?.replace(";", ",")
            ?: ""
        }"
        |${productDetails?.price ?: ""}
        |${productDetails?.priceStandard ?: ""}
        |"${productDetails?.description
            ?.replace("\"", "\"\"")
            ?.replace(";", ",")
            ?.lines()
            ?.joinToString(separator = LINE_SEPARATOR)
            ?: ""
        }"
        |"${productDetails?.images?.joinToString(separator = LINE_SEPARATOR) { it.url }
            ?.lines()
            ?.joinToString(separator = LINE_SEPARATOR)
            ?: ""
        }"
        |${productDetails?.shadeFile ?: ""}"""
            .lines()
            .filterIndexed { index, _ -> index != 0 }
            .joinToString(separator = CSV_SEPARATOR) { it.trimMargin("|") }
            .replace(LINE_SEPARATOR, "\n")
}
