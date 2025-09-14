package cz.drekorian.avonmobilefetcher.flow.catalog

import cz.drekorian.avonmobilefetcher.domain.RuntimeOverride
import cz.drekorian.avonmobilefetcher.domain.hasCatalogs
import cz.drekorian.avonmobilefetcher.errorI18n
import cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsRequest
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.model.Catalog
import kotlinx.coroutines.runBlocking

/**
 * This flow attempts to fetch the list of catalog from the Brochure backend.
 *
 * @see CatalogsRequest
 * @see Catalog
 * @author Marek Osvald
 */
internal class CatalogsFlow(
    private val catalogsRequest: CatalogsRequest,
    private val runtimeOverride: RuntimeOverride,
) {

    companion object {
        private val CATALOG_ID_REGEX = """onclick="open_catalog\(\{url: '(.+)/'""".toRegex()
    }

    /**
     * Attempts to fetch a list of currently available catalogs.
     *
     * @return list of currently available catalogs
     */
    fun fetchCatalogs(): List<Catalog> {
        if (runtimeOverride.hasCatalogs) {
            return getCatalogsFromOverride()
        }

        val foundCatalogs = getCatalogsFromSignpost()
        logger.infoI18n("focus_catalog_acknowledged")
        return (foundCatalogs + Catalog.FOCUS).distinctBy { catalog -> catalog.id }
    }

    private fun getCatalogsFromOverride(): List<Catalog> {
        logger.infoI18n("catalogs_override")
        return runtimeOverride.catalogs.map { id -> Catalog(id) }
    }

    private fun getCatalogsFromSignpost(): List<Catalog> {
        logger.infoI18n("catalogs_request")
        val response = runBlocking { catalogsRequest.send() }
        if (response == null) {
            logger.errorI18n("catalogs_response_null")
            return emptyList()
        }

        val foundCatalogs = CATALOG_ID_REGEX.findAll(response.rawHtml)
            .mapNotNull { matchResult -> matchResult.groups[1]?.value?.let(::Catalog) }
            .distinct()
            .toList()

        logger.infoI18n(
            "catalogs_request_success",
            foundCatalogs.size,
            foundCatalogs.joinToString(separator = ", ") { catalog -> catalog.id }
        )

        return foundCatalogs
    }
}
