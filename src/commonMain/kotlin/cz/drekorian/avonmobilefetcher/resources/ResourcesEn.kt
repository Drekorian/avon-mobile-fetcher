package cz.drekorian.avonmobilefetcher.resources

/**
 * Stores string resources for English user locale.
 *
 * @author Marek Osvald
 */
object ResourcesEn : Resources {

    private val keys = mapOf(
        "welcome" to "AVON mobile fetcher version: %s",
        "unknown_arguments" to "I don't understand these arguments: %s. I'll ignore them and keep going.",
        "override_on" to "Campaign override detected: %s",
        "override_off" to "No campaign override, I will try to determine campaign ID myself.\n             In case I fail, try running me with an override: avon-mobile-fetcher YYYYMM",
        "catalogs_override" to "Catalogs override found.",
        "catalogs_request" to "Fetching the list of catalogs.",
        "catalogs_request_success" to "I have found %s catalogs in total: %s.",
        "catalogs_request_error" to "Fetching the list of catalogs (signpost) data has failed",
        "focus_catalog_acknowledged" to "I also know about the secret Focus catalog.",
        "catalogs_response_null" to "Fetching the list of catalogs failed. Server has returned null.",
        "page_data_request" to "Fetching data for page page %s of catalog %s.",
        "page_data_request_error" to "Fetching data for page %s of catalog %s has failed.",
        "page_data_response_null" to "Fetching data for page %s catalog %s failed. Server has returned null.",
        "page_data_new_product" to "I've found new a new product %s in catalog %s, page %s.",
        "products_request" to "Fetching list of products for catalog %s.",
        "products_request_error" to "Fetching catalog %s has failed.",
        "products_response_null" to "Fetching products for catalog: %s failed. Server has return null.",
        "product_details_request" to "Fetching product details for catalog %s.",
        "product_details_request_error" to "Fetching product has failed.",
        "product_details_response_null" to "Fetching product details for catalog: %s, product: %s failed. Server has return null.",
        "invalid_campaign_override" to "Invalid campaign number override '%s'. Override must be in a YYYYMM format.",
        "failed_to_open_file" to "Failed to open file: %s.",
        "writing_to_disk" to "Writing CSV output to file: %s.",
        "done" to "Done!"
    )

    override operator fun get(key: String) = keys[key] ?: super.get(key)
}
