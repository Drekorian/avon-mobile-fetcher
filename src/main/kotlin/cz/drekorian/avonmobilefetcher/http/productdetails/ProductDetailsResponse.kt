package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.model.Campaign
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
         * Creates [ProductDetailsResponse] using [Campaign] and [ProductDetailsFactory].
         */
        fun fromXml(campaign: Campaign, xml: String, catalogId: String) =
            ProductDetailsResponse(ProductDetailsFactory.createProductDetails(campaign, xml, catalogId))
    }
}
