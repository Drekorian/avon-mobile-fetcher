package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.resources.ResourcesCz
import cz.drekorian.avonmobilefetcher.resources.ResourcesEn
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ResourcesTest {

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `read all keys using reflection`() {
        val enResourcesKeys = ResourcesEn::class.memberProperties.first { it.name == "keys" }
        enResourcesKeys.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val enKeyMap = enResourcesKeys.getter.call() as Map<String, String>

        val czResourcesKeys = ResourcesCz::class.memberProperties.first { it.name == "keys" }
        czResourcesKeys.isAccessible = true
        val czKeyMap = czResourcesKeys.getter.call() as Map<String, String>

        assertEquals(enKeyMap.keys, czKeyMap.keys)
        for ((key, value) in enKeyMap) {
            assertEquals(
                value.split("%s").size,
                ResourcesCz[key].split("%s").size
            )
        }
    }
}