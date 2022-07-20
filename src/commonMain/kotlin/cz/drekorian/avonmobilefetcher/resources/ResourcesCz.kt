package cz.drekorian.avonmobilefetcher.resources

/**
 * Stores string resources for Czech user locales.
 *
 * @author Marek Osvald
 */
object ResourcesCz : Resources {

    @Suppress("SpellCheckingInspection")
    private val keys = mapOf(
        "welcome" to "AVON mobile fetcher native verze: %s",
        "unknown_arguments" to "Nerozumim nasledujicim argumentum: %s. Budu je proto ignorovat a pokracovat bez nich.",
        "override_on" to "Override kampane nalezen: %s",
        "override_off" to "Zadna specifikace kampane, pokusim se detekovat cislo kampane sam.\n     Kdyz se mi to nepovede, zavolej me s overridem: avon-mobile-fetcher YYYYMM",
        "catalogs_override" to "Nalezena specifikace katalogu.",
        "catalogs_request" to "Stahuji seznam katalogu.",
        "catalogs_request_success" to "Nasel jsem %s katalogy: %s.",
        "catalogs_request_error" to "Stahovani seznamu katalogu selhalo.",
        "focus_catalog_acknowledged" to "Take vim o tajnem katalogu Focus.",
        "catalogs_response_null" to "Stahovani seznamu katalogu selhalo. Server vratil null.",
        "page_data_request" to "Stahuji data strany %s katalogu %s.",
        "page_data_request_error" to "Stahovani dat strany %s katalogu %s selhalo.",
        "page_data_response_null" to "Stahovani dat strany %s katalogu %s selhalo. Server vratil null.",
        "page_data_new_product" to "Nalezl jsem novy produkt %s v katalogu %s, na strane %s.",
        "products_request" to "Stahuji produkty katalogu %s.",
        "products_request_error" to "Stahovani katalogu %s selhalo.",
        "products_response_null" to "Stahovani produktu pro katalog: %s selhalo. Server vratil null.",
        "product_details_request" to "Stahuji detaily produktu pro katalog %s.",
        "product_details_request_error" to "Stahovani produktu selhalo.",
        "product_details_response_null" to "Stahovani produktu pro katalog: %s, produkt: %s selhalo. Server vratil null.",
        "invalid_campaign_override" to "Neplatny override cisla kampane '%s'. Override musi být ve formatu YYYYMM.",
        "failed_to_open_file" to "Failed to open file: %s.",
        "writing_to_disk" to "Zapisuji CSV vystup do souboru: %s.",
        "done" to "Hotovo!"
    )

    override operator fun get(key: String) = keys[key] ?: super.get(key)
}
