package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.model.ProductDetails
import kotlinx.serialization.Serializable

/**
 * This class holds the [ProductDetails] data for a single product.
 *
 * @property productDetails stored product details
 * @see ProductDetails
 * @see ProductDetailsRequest
 * @author Marek Osvald
 */
@Serializable
data class ProductDetailsResponse(val productDetails: ProductDetails)
