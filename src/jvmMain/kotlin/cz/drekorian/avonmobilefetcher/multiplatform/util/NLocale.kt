package cz.drekorian.avonmobilefetcher.multiplatform.util

import java.util.Locale

actual val localeUk: NLocale = Locale.UK.toNLocale()

actual val defaultLocale: NLocale
    get() = Locale.getDefault().toNLocale()

private fun Locale.toNLocale(): NLocale = NLocale(
    languageCode = language,
    region = country,
)
