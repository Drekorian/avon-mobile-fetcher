package cz.drekorian.avonmobilefetcher.http.productdetails

import cz.drekorian.avonmobilefetcher.http.BASE_URL
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.model.Image
import cz.drekorian.avonmobilefetcher.model.ProductDetails
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

/**
 * This object constructs [ProductDetails] from XML source.
 *
 * Unlike the signpost HTML, this is valid XML. Therefore productDetails are constructed from XML DOM.
 *
 * @author Marek Osvald
 */
object ProductDetailsFactory {

    private const val ATTRIBUTE_NAME_ID = "id"
    private const val ELEMENT_NAME_TITLE = "title"
    private const val ELEMENT_NAME_IMAGE_FILE = "image_file"
    private const val ELEMENT_NAME_IMAGE = "image"
    private const val ELEMENT_NAME_DESCRIPTION = "description"
    private const val ELEMENT_NAME_VARIANT = "variant"
    private const val ELEMENT_NAME_CATEGORY = "category"
    private const val ELEMENT_NAME_SKU = "sku"
    private const val ELEMENT_NAME_PRICE = "price"
    private const val ELEMENT_NAME_PRICE_STANDARD = "price_standard"

    @Suppress("SpellCheckingInspection")
    private const val ELEMENT_NAME_PHYSICAL_PAGE = "fizical_page" // Sic! Yes, really.
    private const val ELEMENT_NAME_DISPLAY_PAGE = "display_page"
    private const val ELEMENT_NAME_SHADE_FILE = "shade_file"

    private const val SHADE_FILE_DEFAULT = "default.jpg"

    private const val IMAGE_BASE_URL = "$BASE_URL/%s/%s/common/products/images/"
    private const val SHADE_BASE_URL = "$BASE_URL/%s/%s/common/products/shades/"

    private const val LOCALIZED_DECIMAL_SEPARATOR = ','
    private const val NORMALIZED_DECIMAL_SEPARATOR = '.'

    /**
     * Creates [ProductDetails] from given [xml] source and [catalogId].
     *
     * @param campaign current campaign identifier
     * @param xml source XML containing the product details data
     * @param catalogId catalog unique identifier
     */
    fun createProductDetails(campaign: Campaign, xml: String, catalogId: String): ProductDetails {
        val document = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(InputSource(StringReader(xml)))

        val root: Element = document.documentElement

        // add image_file to the list of images
        val images = mutableListOf<Image>()
        val imageFile = root.getElementsByTagName(ELEMENT_NAME_IMAGE_FILE)
        if (imageFile.length > 0) {
            images.add(
                Image(
                    "${IMAGE_BASE_URL.format(campaign.toRestfulArgument(), catalogId)}${imageFile.item(0).textContent}"
                )
            )
        }

        // add images to the list of images
        val imageElements = root.getElementsByTagName(ELEMENT_NAME_IMAGE)
        images.addAll((0 until imageElements.length).map { index ->
            Image(
                "${IMAGE_BASE_URL.format(campaign.toRestfulArgument(), catalogId)}${imageElements.item(index).textContent}"
            )
        })

        return ProductDetails(
            id = root.getAttribute(ATTRIBUTE_NAME_ID),
            images = images.distinct(),
            title = root.getElementsByTagName(ELEMENT_NAME_TITLE).item(0).textContent,
            description = root.getElementsByTagName(ELEMENT_NAME_DESCRIPTION).item(0).textContent,
            variant = root.getElementsByTagName(ELEMENT_NAME_VARIANT).item(0)?.textContent ?: "",
            category = root.getElementsByTagName(ELEMENT_NAME_CATEGORY)?.item(0)?.textContent ?: "",
            sku = root.getElementsByTagName(ELEMENT_NAME_SKU).item(0).textContent,
            price = root.getElementsByTagName(ELEMENT_NAME_PRICE).item(0).textContent.replace(
                oldChar = LOCALIZED_DECIMAL_SEPARATOR,
                newChar = NORMALIZED_DECIMAL_SEPARATOR
            ),
            priceStandard = root.getElementsByTagName(ELEMENT_NAME_PRICE_STANDARD).item(0).textContent.replace(
                oldChar = LOCALIZED_DECIMAL_SEPARATOR,
                newChar = NORMALIZED_DECIMAL_SEPARATOR
            ),
            physicalPage = root.getElementsByTagName(ELEMENT_NAME_PHYSICAL_PAGE).item(0)?.textContent?.toInt() ?: 0,
            displayPage = root.getElementsByTagName(ELEMENT_NAME_DISPLAY_PAGE).item(0)?.textContent ?: "",
            shadeFile = root.getElementsByTagName(ELEMENT_NAME_SHADE_FILE).item(0)?.textContent?.let { shadeFile ->
                when (shadeFile) {
                    SHADE_FILE_DEFAULT -> ""
                    else -> "${SHADE_BASE_URL.format(campaign.toRestfulArgument(), catalogId)}$shadeFile"
                }
            } ?: ""
        )
    }
}
