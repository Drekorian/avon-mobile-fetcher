package cz.drekorian.avonmobilefetcher.multiplatform.util

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual val localeUk: NLocale = NLocale("en", "GB")

actual val defaultLocale: NLocale
    get() = with(NSLocale.currentLocale) {
        NLocale(
            languageCode = languageCode,
            region = countryCode ?: "",
        )
    }
