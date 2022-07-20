package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsOverride
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MainTest {

    @Suppress("SpellCheckingInspection")
    @Test
    fun `common parameter names are valid`() {
        val commonNames = listOf("katalog", "darkovy-katalog", "plet", "focus", "distillery")
        val catalogsParameter = "--catalogs=${commonNames.joinToString(separator = ",")}"

        processCatalogsOverride(distinctArgs = mutableSetOf(catalogsParameter))
        assertEquals(commonNames.sorted(), CatalogsOverride.catalogs.sorted())

        CatalogsOverride.setCatalogs(emptyList())
    }
}
