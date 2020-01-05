package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.CsvPrinter
import cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsRequest
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Record

class MasterFlow {

    companion object {

        private const val FILE_NAME = "AvonCatalogs%s.csv"
        private const val CAMPAIGN_ID_LENGTH = 2
        private const val CAMPAIGN_ID_PADDING_START = '0'
    }

    fun execute() {
        val catalogs = CatalogsFlow().fetchCatalogs()

        val mainCatalog = catalogs.first().name
        val campaign = Campaign.fromMainCatalogName(mainCatalog)

        val productFlow = ProductFlow()
        val catalogWithProducts = catalogs.map { catalog ->
            catalog to productFlow.fetchProducts(catalog)
        }

        val records = catalogWithProducts.flatMap { (catalog, products) ->
            products.mapNotNull { product ->
                val response = ProductDetailsRequest().send(catalog, product)
                if (response == null) {
                    // TODO: log
                    return@mapNotNull null
                }

                Record(catalog, product, response.productDetails).toCsv(campaign)
            }
        }

        // create output file name
        val fileName = FILE_NAME.format(
            "${campaign.year}${campaign.id.padStart(CAMPAIGN_ID_LENGTH, CAMPAIGN_ID_PADDING_START)}"
        )

        CsvPrinter.print(
            fileName,
            records,
            "Year",
            "Campaign",
            "Catalog",
            "Category",
            "Category 2",
            "Physical Page",
            "Physical Page 2",
            "Display Page",
            "Display Page 2",
            "ID",
            "ID 2",
            "SKU",
            "Title",
            "Title 2",
            "Variant",
            "Price",
            "Price Standard",
            "Description",
            "Images",
            "Shade File"
        )
    }
}
