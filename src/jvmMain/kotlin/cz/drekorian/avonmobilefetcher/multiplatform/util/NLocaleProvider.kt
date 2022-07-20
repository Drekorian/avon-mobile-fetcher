package cz.drekorian.avonmobilefetcher.multiplatform.util

import java.util.Locale

actual object NLocaleProvider {

    actual val UK: NLocale = Locale.UK.toNLocale()

    actual fun getDefault(): NLocale = Locale.getDefault().toNLocale()

    private fun Locale.toNLocale(): NLocale = NLocale(
        languageCode = language,
        region = country,
    )
}
