package cz.drekorian.avonmobilefetcher.flow.catalog

/**
 * This object handles manual catalog IDs override.
 *
 * @property catalogs stores catalog IDs override
 * @author Marek Osvald
 */
object CatalogsOverride {

    val catalogs = mutableListOf<String>()

    val hasOverride: Boolean
        get() = catalogs.isNotEmpty()

    /**
     * Sets catalogs override.
     *
     * @param catalogs catalog IDs override
     */
    fun setCatalogs(catalogs: Collection<String>) {
        with(this.catalogs) {
            clear()
            addAll(catalogs)
        }
    }
}
