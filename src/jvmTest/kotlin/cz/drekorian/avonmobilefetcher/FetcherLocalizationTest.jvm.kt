package cz.drekorian.avonmobilefetcher

import com.github.ajalt.clikt.output.Localization
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.functions
import kotlin.reflect.full.valueParameters
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FetcherLocalizationTest {

    @Test
    fun `fetcher english localization should be equal to the default one`() {
        val defaultLocalization = object : Localization {}
        val fetcherLocalization = FetcherLocalization()

        defaultLocalization::class.declaredMemberFunctions
            .forEach { function ->
                val params = function.valueParameters.map { param ->
                    when (param.type.classifier) {
                        List::class -> listOf("first", "second")
                        Int::class -> param.index
                        else -> "param${param.index}"
                    }
                }.toTypedArray()

                assertEquals(
                    function.call(defaultLocalization, *params),
                    fetcherLocalization::class.functions.first {
                        it.name == function.name &&
                                it.valueParameters.size == function.valueParameters.size
                    }.call(fetcherLocalization, *params)
                )
            }
    }
}
