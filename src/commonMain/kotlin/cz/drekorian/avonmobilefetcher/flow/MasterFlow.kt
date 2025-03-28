package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.debugI18n
import cz.drekorian.avonmobilefetcher.domain.CampaignRepository
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsFlow
import cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsRequest
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Record
import cz.drekorian.avonmobilefetcher.nFormat
import cz.drekorian.avonmobilefetcher.printCsv
import kotlinx.coroutines.runBlocking

/**
 * This flow handles the main logic of the fetcher script.
 *
 * 1) Downloads catalogs from [CatalogsFlow].
 * 2) Downloads products from [ProductsFlow].
 * 3) Maps catalogs and products into [Record]s.
 * 4) Prints the records' data via [printCsv].
 *
 * @see CatalogsFlow
 * @see ProductsFlow
 * @see Record
 * @see printCsv
 * @author Marek Osvald
 */
internal class MasterFlow(
    private val campaignRepository: CampaignRepository,
    private val catalogsFlow: CatalogsFlow,
    private val productDetailsRequest: ProductDetailsRequest,
    private val productsFlow: ProductsFlow,
) {

    companion object {

        private const val FILE_NAME = "AvonCatalogs%s.csv"
        private const val CAMPAIGN_ID_LENGTH = 2
        private const val CAMPAIGN_ID_PADDING_START = '0'
    }

    /**
     * Executes this [MasterFlow].
     */
    fun execute() {
        val catalogs = catalogsFlow.fetchCatalogs()

        val campaign = campaignRepository.getCurrentCampaign()

        val catalogWithProducts = catalogs.map { catalog ->
            catalog to productsFlow.fetchProducts(campaign, catalog)
        }

        val records = catalogWithProducts.flatMap { (catalog, products) ->
            logger.infoI18n("product_details_request", catalog.id)
            products.map { product ->
                val response = try {
                    runBlocking { productDetailsRequest.send(campaign, catalog, product) }
                } catch (_: Exception) {
                    logger.debugI18n("product_details_response_null", catalog.id, product.id)
                    null
                }

                Record(catalog, product, response?.productDetails).toCsv(campaign)
            }
        }

        // create output file name
        val fileName = FILE_NAME.nFormat(
            "${campaign.year}${campaign.id.padStart(CAMPAIGN_ID_LENGTH, CAMPAIGN_ID_PADDING_START)}"
        )
        logger.infoI18n("writing_to_disk", fileName)

        printCsv(
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
            "Shade File",
            "Unit Volume",
            "Unit Measure",
        )

        logger.infoI18n("done")
    }
}
