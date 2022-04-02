package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.CsvPrinter
import cz.drekorian.avonmobilefetcher.debugI18n
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsFlow
import cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsRequest
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Record
import kotlinx.coroutines.runBlocking

/**
 * This flow handles the main logic of the fetcher script.
 *
 * 1) Downloads catalogs from [CatalogsFlow].
 * 2) Downloads products from [ProductsFlow].
 * 3) Maps catalogs and products into [Record]s.
 * 4) Feeds the records data into [CsvPrinter].
 *
 * @see CatalogsFlow
 * @see ProductsFlow
 * @see Record
 * @see CsvPrinter
 * @author Marek Osvald
 */
class MasterFlow {

    companion object {

        private const val FILE_NAME = "AvonCatalogs%s.csv"
        private const val CAMPAIGN_ID_LENGTH = 2
        private const val CAMPAIGN_ID_PADDING_START = '0'
    }

    /**
     * Executes this [MasterFlow].
     */
    fun execute() {
        val catalogs = CatalogsFlow().fetchCatalogs()

        val campaign = Campaign.getCurrentCampaign()

        val productFlow = ProductsFlow()
        val catalogWithProducts = catalogs.map { catalog ->
            catalog to productFlow.fetchProducts(campaign, catalog)
        }

        val records = catalogWithProducts.flatMap { (catalog, products) ->
            logger.infoI18n("product_details_request", catalog)
            products.map { product ->
                val response = try {
                    runBlocking { ProductDetailsRequest().send(campaign, catalog, product) }
                } catch (_: Exception) {
                    null
                }
                
                if (response == null) {
                    logger.debugI18n("product_details_response_null", catalog.id, product.id)
                }

                Record(catalog, product, response?.productDetails).toCsv(campaign)
            }
        }

        // create output file name
        val fileName = FILE_NAME.format(
            "${campaign.year}${campaign.id.padStart(CAMPAIGN_ID_LENGTH, CAMPAIGN_ID_PADDING_START)}"
        )
        logger.infoI18n("writing_to_disk", fileName)

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

        logger.infoI18n("done")
    }
}
