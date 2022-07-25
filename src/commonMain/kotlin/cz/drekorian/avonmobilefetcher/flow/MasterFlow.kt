package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.CsvPrinter
import cz.drekorian.avonmobilefetcher.debugI18n
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsFlow
import cz.drekorian.avonmobilefetcher.flow.linenumber.LineNumberFlow
import cz.drekorian.avonmobilefetcher.http.login.LoginRequest
import cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsRequest
import cz.drekorian.avonmobilefetcher.http.validate.ValidationRequest
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product
import cz.drekorian.avonmobilefetcher.model.Record
import cz.drekorian.avonmobilefetcher.model.Validation
import cz.drekorian.avonmobilefetcher.nFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

/**
 * This flow handles the main logic of the fetcher script.
 *
 * 1) Downloads catalogs from [CatalogsFlow].
 * 2) Downloads products from [ProductsFlow].
 * 3) Maps catalogs and products into [Record]s.
 * 4) Feeds the records' data into [CsvPrinter].
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

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Executes this [MasterFlow].
     */
    fun execute() {
        val catalogs = CatalogsFlow().fetchCatalogs()

        val campaign = Campaign.getCurrentCampaign()

        val productFlow = ProductsFlow()

        val catalogWithProducts = catalogs.map { catalog -> catalog to productFlow.fetchProducts(campaign, catalog) }

        // try login
        val loginResult = runBlocking { LoginRequest().send() }
        println("LOGIN RESULT: ${loginResult.loginResult}")

        val lineNumbers = LineNumberFlow().fetchLineNumbers()
        println(lineNumbers.joinToString(separator = "\n"))
        val validatedLineNumbers: Map<String, Validation> = runBlocking {
            lineNumbers
//            lineNumbers.chunked(10)
                .map { chunk ->
                    coroutineScope.async { ValidationRequest().send(campaign, listOf(chunk)) }
                }
                .awaitAll()
                .flatten()
                .map { itemValidation -> Validation.from(itemValidation) }
                .associateBy { validation -> validation.sku }
        }

        val records = buildList {
            catalogWithProducts.flatMap { (catalog, products) ->
                logger.infoI18n("product_details_request", catalog.id)
                products.map { product ->
                    val productDetailsResponse = try {
                        runBlocking { ProductDetailsRequest().send(campaign, catalog, product) }
                    } catch (_: Exception) {
                        null
                    }

                    if (productDetailsResponse == null) {
                        logger.debugI18n("product_details_response_null", catalog.id, product.id)
                    }

                    val validation = validatedLineNumbers[product.id]
                    add(Record(catalog, product, productDetailsResponse?.productDetails, validation))
                }
            }

            val catalogedProducts = catalogWithProducts
                .flatMap { (_, product) -> product }
                .map { product -> product.id }

            val orphanedValidations = validatedLineNumbers
                .asSequence()
                .filter { validation -> validation.value.result == "VALID" }
                .filter { (key, _) -> key !in catalogedProducts }
                .toList()

            for ((key, product) in orphanedValidations) {
                add(
                    Record(
                        catalog = Catalog.NONE,
                        product = Product(
                            id = product.sku,
                            title = product.productName,
                        ),
                        productDetails = null,
                        validation = validatedLineNumbers[key],
                    )
                )
            }
        }.map { record -> record.toCsv(campaign) }

        // create output file name
        val fileName = FILE_NAME.nFormat(
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
            "Shade File",
            "Unit Volume",
            "Unit Measure",
            "Validation",
        )

        logger.infoI18n("done")
    }
}
