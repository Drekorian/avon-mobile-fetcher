package cz.drekorian.avonmobilefetcher.flow

import cz.drekorian.avonmobilefetcher.http.catalogs.CatalogsRequest
import cz.drekorian.avonmobilefetcher.model.Catalog

class CatalogsFlow {

    companion object {
        private val CATALOG_ID_REGEX = """<img class="img-responsive".* onclick="open_catalog\('(.+)/index\.html'\)""".toRegex()
        private const val CATALOG_NAME_REGEX = """<p style="width:100%;font-size:20px;font-weight:bold;color:#eb1194;text-align:center;font-family:Arial" >(.+)</p>"""
    }

    /**
     * Attempts to fetch a list of currently available catalogs.
     *
     * @return list of currently available catalogs
     */
    fun fetchCatalogs(): List<Catalog> {
        val response = CatalogsRequest().send()
        if (response == null) {
            // TODO: log
            return emptyList()
        }

        val ids = CATALOG_ID_REGEX
            .findAll(response.rawHtml)
            .toList()
            .mapNotNull { it.groups[1]?.value }
            .distinct()

        return ids.withIndex().mapNotNull { (index, id) ->
            val title = CATALOG_NAME_REGEX
                .toRegex()
                .findAll(response.rawHtml)
                .map { it.groups[1]?.value?.trim() } // HTML may contain whitespace before the closing tag
                .distinct()
                .toList()

            Catalog(id, title[index] ?: return@mapNotNull null)
        }
    }
}
