package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.I18n
import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.http.KHttpClient
import cz.drekorian.avonmobilefetcher.http.Request
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product

class ProductDetailsRequest: Request() {

    companion object {
        private const val URL = "$BASE_URL/%s/common/products/xml/%s.xml"
    }

    fun send(catalog: Catalog, product: Product): ProductDetailsResponse? {

        val response = KHttpClient.get(URL.format(catalog.id, product.id))

        if (!checkStatusCode(response, logger, I18n.get("product_details_request_error"))) {
            return null
        }

        return ProductDetailsResponse(response.text, catalog.id)
    }
}
