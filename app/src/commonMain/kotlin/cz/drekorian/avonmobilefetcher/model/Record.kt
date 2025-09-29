package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.CSV_SEPARATOR
import cz.drekorian.avonmobilefetcher.productsapi.Product as ProductApiProduct

/**
 * This data class stores the combined info about product and product details.
 *
 * @property catalog product catalog
 * @property product product data
 * @author Marek Osvald
 */
data class Record(
    private val catalog: Catalog,
    private val product: ProductApiProduct,
    private val page: String,
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
        |$page
        |="${product.sku.padStart(PRODUCT_ID_LENGTH, PRODUCT_ID_PADDING)}"
        |"${
            product.title
                .replace(";", ",")
                .lines()
                .joinToString(separator = LINE_SEPARATOR)
        }"
        |${product.price}
        |${product.priceStandard}
        |"${
            product.description
                .replace("\"", "\"\"")
                .replace(";", ",")
                .lines()
                .joinToString(separator = LINE_SEPARATOR)
        }""""
            .lines()
            .filterIndexed { index, _ -> index != 0 }
            .joinToString(separator = CSV_SEPARATOR) { it.trimMargin("|") }
            .replace(LINE_SEPARATOR, "\n")
}
