package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.domain.CampaignRepository
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsFlow
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Record
import cz.drekorian.avonmobilefetcher.nFormat
import cz.drekorian.avonmobilefetcher.printCsv
import cz.drekorian.avonmobilefetcher.productsapi.GetProductPagesUseCase
import cz.drekorian.avonmobilefetcher.productsapi.GetProductsUseCase
import cz.drekorian.avonmobilefetcher.settingsapi.GetSettingsUseCase
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
    private val getSettings: GetSettingsUseCase,
    private val getProducts: GetProductsUseCase,
    private val getProductPages: GetProductPagesUseCase,
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

        runBlocking {
            val brochures = getSettings(campaign)

            val records = catalogs.zip(brochures).flatMap { (catalog, brochure) ->
                getProducts(brochure).flatMap { product ->
                    getProductPages(brochure, product.sku).map { pageNumber ->
                        Record(
                            catalog = catalog,
                            product = product,
                            page = pageNumber,
                        ).toCsv(campaign)
                    }
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
                "Page",
                "SKU",
                "Title",
                "Price",
                "Price Standard",
                "Description",
            )
        }

        logger.infoI18n("done")
    }
}
