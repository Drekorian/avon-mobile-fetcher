package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsOverride
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MainTest {

    @Test
    fun `common parameter names are valid`() {
        @Suppress("SpellCheckingInspection")
        val commonNames = listOf("katalog", "darkovy-katalog", "plet", "focus", "distillery")

        CatalogsOverride.setCatalogs(commonNames)
        assertEquals(commonNames, CatalogsOverride.catalogs)

        CatalogsOverride.setCatalogs(emptyList())
    }
}
