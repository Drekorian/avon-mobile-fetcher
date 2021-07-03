package cz.drekorian.avonmobilefetcher.flow.catalog

import cz.drekorian.avonmobilefetcher.errorI18n
import cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsRequest
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Catalog

/**
 * This flow attempts to fetch the list of catalog from the Brochure backend.
 *
 * @see CatalogsRequest
 * @see Catalog
 * @author Marek Osvald
 */
class CatalogsFlow {

    companion object {
        private val CATALOG_ID_REGEX = """onclick="open_catalog\(\{url: '(.+)/index\.html'""".toRegex()
        private val CATALOG_NAME_REGEX = """<span class="my-auto">(.+)</span>""".toRegex()
    }

    /**
     * Attempts to fetch a list of currently available catalogs.
     *
     * @return list of currently available catalogs
     */
    fun fetchCatalogs(): List<Catalog> {
        val foundCatalogs = when {
            CatalogsOverride.catalogs.isNotEmpty() -> getCatalogsFromOverride()
            else -> getCatalogsFromSignpost()
        }

        logger.infoI18n("focus_catalog_acknowledged")
        return (foundCatalogs + Catalog.FOCUS).distinctBy { catalog -> catalog.id }
    }

    private fun getCatalogsFromOverride(): List<Catalog> {
        logger.infoI18n("catalogs_override")
        return CatalogsOverride.catalogs.map { id -> Catalog(id = id, name = id) }
    }

    private fun getCatalogsFromSignpost(): List<Catalog> {
        logger.infoI18n("catalogs_request")
        val response = CatalogsRequest().send()
        if (response == null) {
            logger.errorI18n("catalogs_response_null")
            return emptyList()
        }

        val catalogIds = CATALOG_ID_REGEX.findAll(response.rawHtml)
        val catalogNames = CATALOG_NAME_REGEX.findAll(response.rawHtml)

        val foundCatalogs =
            catalogIds.mapNotNull { it.groups[1]?.value }.distinct()
            .zip(catalogNames.mapNotNull { it.groups[1]?.value }.distinct())
            .map { (id, name) -> Catalog(id = id, name = name.trim()) }
            .toList()

        logger.infoI18n(
            "catalogs_request_success",
            foundCatalogs.size,
            foundCatalogs.joinToString(separator = ", ") { catalog -> catalog.name }
        )

        return foundCatalogs
    }
}
