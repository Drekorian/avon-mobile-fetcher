package cz.drekorian.avonmobilefetcher.http.products

import cz.drekorian.avonmobilefetcher.model.Product
import kotlinx.serialization.Serializable

/**
 * This class holds the list of products of a catalog.
 *
 * @property products list of parsed products
 * @see Product
 * @see ProductsRequest
 * @author Marek Osvald
 */
@Serializable
data class ProductsResponse(
    val products: List<Product>,
)
