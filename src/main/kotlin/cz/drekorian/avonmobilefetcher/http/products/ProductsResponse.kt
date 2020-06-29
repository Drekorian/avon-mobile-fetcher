package cz.drekorian.avonmobilefetcher.http.products

import cz.drekorian.avonmobilefetcher.model.Product
import org.json.JSONArray
import org.json.JSONObject

/**
 * This class holds the list of products of a catalog.
 *
 * @property products list of parsed products
 * @see Product
 * @see ProductsRequest
 * @author Marek Osvald
 */
class ProductsResponse(data: JSONArray) {

    val products: List<Product> = data.map { productRaw -> Product(productRaw as JSONObject) }
}
