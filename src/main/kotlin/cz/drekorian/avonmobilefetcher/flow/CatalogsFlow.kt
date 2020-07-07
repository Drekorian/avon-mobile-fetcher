package cz.drekorian.avonmobilefetcher.flow

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
        private val CATALOG_ID_REGEX =
            """<img class="img-responsive".* onclick="open_catalog\(.+url:'(.+)/index\.html'""".toRegex()
        private val CATALOG_NAME_REGEX =
            """<p style="width:100%;font-size:20px;font-weight:bold;color:#ff336d;text-align:center;font-family:Arial" >(.+)</p>"""
                .toRegex()
    }

    /**
     * Attempts to fetch a list of currently available catalogs.
     *
     * @return list of currently available catalogs
     */
    fun fetchCatalogs(): List<Catalog> {
        logger.infoI18n("catalogs_request")
        val response = CatalogsRequest().send()
        if (response == null) {
            logger.errorI18n("catalogs_response_null")
            return emptyList()
        }

        val ids = CATALOG_ID_REGEX
            .findAll(response.rawHtml)
            .asSequence()
            .mapNotNull { it.groups[1]?.value }
            .distinct()
            .toList()

        return ids.withIndex().mapNotNull { (index, id) ->
            val title = CATALOG_NAME_REGEX
                .findAll(response.rawHtml)
                .asSequence()
                .map { it.groups[1]?.value?.trim() } // HTML may contain whitespace before the closing tag
                .distinct()
                .toList()

            Catalog(id, title[index] ?: return@mapNotNull null)
        }.also { catalogs ->
            logger.infoI18n(
                "catalogs_request_success",
                catalogs.size,
                catalogs.joinToString(separator = ", ") { catalog -> catalog.name }
            )
        }
    }
}
