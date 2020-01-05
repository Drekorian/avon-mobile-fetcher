package cz.drekorian.avonmobilefetcher.http.catalog

import cz.drekorian.avonmobilefetcher.model.Product
import org.json.JSONArray
import org.json.JSONObject

class CatalogResponse(data: JSONArray) {

    val products: List<Product> = data.map { productRaw -> Product(productRaw as JSONObject) }
}
