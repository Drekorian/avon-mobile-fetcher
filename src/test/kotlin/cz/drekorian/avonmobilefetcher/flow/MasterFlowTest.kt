package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.http.productdetails.ProductDetailsResponse
import cz.drekorian.avonmobilefetcher.model.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
            <product id="12345">
                <image_file><![CDATA[prod_5519795_1_613x613.jpg]]></image_file>
                <title><![CDATA[Test product 1]]></title>
                <description><![CDATA[Test description]]></description>
                <variant><![CDATA[]]></variant>
                <category><![CDATA[Test Category]]></category>
                <sku><![CDATA[12345]]></sku>
                <price><![CDATA[129,90]]></price>
                <price_standard><![CDATA[359,90]]></price_standard>
                <fizical_page><![CDATA[68]]></fizical_page>
                <display_page><![CDATA[68]]></display_page>
                <shade_file><![CDATA[default.jpg]]></shade_file>
            </product>
        """

        private val PRODUCT_DETAILS_FIRST_CSV: String = """
            2020;01;katalog;0;Test Category;42;68;42;68;="12345";="12345";="12345";"Test Product 1";"Test product 1";"";129.90;359.90;"Test description";"https://cz.avon-brochure.com/c01_cz_2020/katalog/common/products/images/prod_5519795_1_613x613.jpg";
        """.trimIndent()

        private val PRODUCT_DETAILS_SECOND_CSV: String = """
            2020;01;katalog;1;;68;0;68;;="54321";="";="";"Test Product 2";"";"";;;"";"";
        """.trimIndent()
    }

    @Test
    fun `product records are properly serialized for both valid an null product details`() {
        // arrange
        val campaign = Campaign("2020", "01")
        val catalog = Catalog("katalog")

        val products = listOf<Product>(
            Json.decodeFromString(PRODUCT_FIRST_RAW),
            Json.decodeFromString(PRODUCT_SECOND_RAW),
        )

        // act
        val records = products.withIndex().map { (index, product) ->
            val productDetails: ProductDetails? = when (index) {
                0 -> ProductDetailsResponse.fromXml(campaign, PRODUCT_FIRST_DETAILS_RAW, "katalog").productDetails
                else -> null
            }

            return@map Record(
                catalog = catalog,
                product = product,
                productDetails = productDetails
            )
        }

        // assert
        assertThat(records[0].toCsv(campaign)).isEqualTo(PRODUCT_DETAILS_FIRST_CSV)
        assertThat(records[1].toCsv(campaign)).isEqualTo(PRODUCT_DETAILS_SECOND_CSV)
    }
}
