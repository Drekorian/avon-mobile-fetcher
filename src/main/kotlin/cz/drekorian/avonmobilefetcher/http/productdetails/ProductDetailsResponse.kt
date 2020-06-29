package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.model.ProductDetails

/**
 * This class holds the [ProductDetails] data for a single product.
 *
 * @property productDetails stored product details
 * @see ProductDetails
 * @see ProductDetailsFactory
 * @see ProductDetailsRequest
 * @author Marek Osvald
 */
class ProductDetailsResponse private constructor(val productDetails: ProductDetails) {

    companion object {

        /**
         * Creates [ProductDetailsResponse] using [ProductDetailsFactory].
         */
        fun fromXml(xml: String, catalogId: String) =
            ProductDetailsResponse(ProductDetailsFactory.createProductDetails(xml, catalogId))
    }
}
