package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Catalog
import cz.drekorian.avonmobilefetcher.model.Product
import cz.drekorian.avonmobilefetcher.model.ProductDetails
import cz.drekorian.avonmobilefetcher.model.Record
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * An integration test class for [MasterFlow].
 *
 * @author Marek Osvald
 */
@Suppress("SpellCheckingInspection")
class MasterFlowTest {

    companion object {

        private const val PRODUCT_FIRST_RAW = """
        {
            "id": "12345",
            "title": "Test Product 1",
            "category": "0",
            "fizical_page": 42,
            "display_page": "42"
        }"""

        private const val PRODUCT_SECOND_RAW = """
        {
                "id": "54321",
                "title": "Test Product 2",
                "category": "1",
                "fizical_page": 68,
                "display_page": "68"
            }
        """

        private const val PRODUCT_FIRST_DETAILS_RAW: String = """
            {
                "category": "Test Category",
                "description": "Test description",
                "fsc": "1234567",
                "sku": "12345",
                "price_standard": "359",
                "title": "Test product 1",
                "concept_code": "9876543",
                "price": "129",
                "unit_measure": "ml",
                "unit_number": "250",
                "variant": "",
                "display_page": "68",
                "fizical_page": 68,
                "image_file": "cz/c07_cz_2022/prod_1226821_1_613x613.jpg?v=1654862200",
                "images": ["cz/c07_cz_2022/prod_1226821_1_613x613.jpg?v=1654862200"],
                "shade_file":"default-shade.jpg?v=1621934601"
            }
        """

        private val PRODUCT_DETAILS_FIRST_CSV: String = """
            2020;01;katalog;0;Test Category;42;68;42;68;="12345";="12345";="12345";"Test Product 1";"Test product 1";"";129;359;"Test description";"https://media.ce.avon.digital-catalogue.com/cz/c07_cz_2022/prod_1226821_1_613x613.jpg?v=1654862200";"https://media.ce.avon.digital-catalogue.com/default-shade.jpg?v=1621934601";="250";ml
        """.trimIndent()

        private val PRODUCT_DETAILS_SECOND_CSV: String = """
            2020;01;katalog;1;;68;0;68;;="54321";="";="";"Test Product 2";"";"";;;"";"";"";="";
        """.trimIndent()

        private val json = Json { ignoreUnknownKeys = true }
    }

    @Test
    fun `product records are properly serialized for both valid an null product details`() {
        // arrange
        val campaign = Campaign("2020", "01")
        val catalog = Catalog("katalog")

        val products = listOf<Product>(
            json.decodeFromString(PRODUCT_FIRST_RAW),
            json.decodeFromString(PRODUCT_SECOND_RAW),
        )

        // act
        val records = products.withIndex().map { (index, product) ->
            val productDetails: ProductDetails? = when (index) {
                0 -> json.decodeFromString<ProductDetails>(PRODUCT_FIRST_DETAILS_RAW)
                else -> null
            }

            return@map Record(
                catalog = catalog,
                product = product,
                productDetails = productDetails
            )
        }

        // assert
        assertEquals(PRODUCT_DETAILS_FIRST_CSV, records[0].toCsv(campaign))
        assertEquals(PRODUCT_DETAILS_SECOND_CSV, records[1].toCsv(campaign))
    }
}
