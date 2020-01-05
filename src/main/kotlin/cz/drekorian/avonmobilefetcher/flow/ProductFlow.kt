package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.http.catalog.CatalogRequest
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product

class ProductFlow {

    fun fetchProducts(catalog: Catalog) : List<Product> {

        val response = CatalogRequest().send(catalog.id)
        if (response == null) {
            // TODO: log
            return listOf()
        }

        return response.products
    }
}