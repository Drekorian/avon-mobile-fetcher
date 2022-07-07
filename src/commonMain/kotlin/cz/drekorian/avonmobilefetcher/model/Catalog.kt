package cz.drekorian.avonmobilefetcher.model

/**
 * This data class stores values about the Catalog.
 *
 * @property id unique identifier of the catalog
 * @author Marek Osvald
 */
data class Catalog(val id: String) {

    companion object {

        private const val FOCUS_ID = "focus"

        /**
         * Creates a new instance of the Focus catalog.
         *
         * This particular catalog is available at [https://cz.avon-brochure.com/focus] and isn't available at the
         * brochure signpost page. It's UI layer is actually password protected. XML/JSON endpoints are not protected
         * and are accessible just like the other catalogs.
         */
        val FOCUS = Catalog(FOCUS_ID)
    }
}
