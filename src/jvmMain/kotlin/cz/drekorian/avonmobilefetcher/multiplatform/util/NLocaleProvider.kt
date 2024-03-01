package cz.drekorian.avonmobilefetcher.multiplatform.util

import java.util.Locale

actual val UK: NLocale = Locale.UK.toNLocale()

actual fun getDefaultLocale(): NLocale = Locale.getDefault().toNLocale()

private fun Locale.toNLocale(): NLocale = NLocale(
    languageCode = language,
    region = country,
)
