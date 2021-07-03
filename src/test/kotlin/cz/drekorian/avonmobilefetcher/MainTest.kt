package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsOverride
import org.junit.jupiter.api.Test

internal class MainTest {

   @Suppress("SpellCheckingInspection")
    @Test
    fun `common parameter names are valid`() {
        val commonNames = listOf("katalog", "darkovy-katalog", "plet", "focus", "distillery")
        val catalogsParameter = "--catalogs=${commonNames.joinToString(separator = ",")}"

       processCatalogsOverride(distinctArgs = mutableSetOf(catalogsParameter))
       assert(CatalogsOverride.catalogs.sorted() == commonNames.sorted())

       CatalogsOverride.setCatalogs(emptyList())
    }
}
