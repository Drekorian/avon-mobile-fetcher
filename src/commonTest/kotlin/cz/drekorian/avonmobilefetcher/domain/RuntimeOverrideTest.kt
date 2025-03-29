package cz.drekorian.avonmobilefetcher.domain

import cz.drekorian.avonmobilefetcher.CommonTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RuntimeOverrideTest : CommonTest() {

   private val runtimeOverride = RuntimeOverride()

    @Test
    fun `when campaign is set it should be stored`() {
        assertEquals(
            "",
            runtimeOverride.campaign
        )

        runtimeOverride.campaign = "202503"
        assertEquals(
            "202503",
            runtimeOverride.campaign
        )

        runtimeOverride.campaign = ""
        assertEquals(
            "",
            runtimeOverride.campaign
        )
    }

    @Test
    fun `when invalid campaign override is set it should not be stored`() {
        // given
        assertEquals(
            "",
            runtimeOverride.campaign
        )

        // when
        runtimeOverride.campaign = "abcdef"

        // then
        assertEquals(
            "",
            runtimeOverride.campaign
        )
    }

    @Test
    fun `when campaign override is empty hasCampaign should return false`() {
        assertEquals(
            false,
            runtimeOverride.hasCampaign
        )
    }

    @Test
    fun `when campaign override is not empty hasCampaign should return true`() {
        // when
        runtimeOverride.campaign = "202503"

        // then
        assertEquals(
            true,
            runtimeOverride.hasCampaign
        )
    }

    @Test
    fun `when catalog is set it should be stored`() {
        assertEquals(
            emptyList(),
            runtimeOverride.catalogs
        )

        runtimeOverride.catalogs = listOf("catalog1", "catalog2")
        assertEquals(
            listOf("catalog1", "catalog2"),
            runtimeOverride.catalogs
        )

        runtimeOverride.catalogs = emptyList()
        assertEquals(
            emptyList(),
            runtimeOverride.catalogs
        )
    }

    @Test
    fun `when catalog override is empty hasCatalogs should return false`() {
        assertEquals(
            false,
            runtimeOverride.hasCatalogs
        )
    }

    @Test
    fun `when catalog override is not empty hasCatalogs should return true`() {
        // when
        runtimeOverride.catalogs = listOf("catalog1", "catalog2")

        // then
        assertEquals(
            true,
            runtimeOverride.hasCatalogs
        )
    }
}
