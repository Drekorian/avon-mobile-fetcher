package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.http.validate.ItemValidation

data class Validation(
    val productName: String,
    val result: String,
    val sku: String,
) {

    companion object {

        fun from(itemValidation: ItemValidation): Validation = Validation(
            result = if (itemValidation.valid) "VALID" else itemValidation.reasonCode,
            productName = itemValidation.productName,
            sku = itemValidation.lineNumber
        )
    }
}
