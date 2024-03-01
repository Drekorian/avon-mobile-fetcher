package cz.drekorian.avonmobilefetcher.multiplatform.util

import platform.windows.GetUserDefaultLangID

private const val LANG_ID_CZECH = 1029
private const val LANG_ID_UK = 2057

actual val UK: NLocale = NLocale("en", "GB")

actual fun getDefaultLocale(): NLocale = when (GetUserDefaultLangID().toInt()) {
    LANG_ID_CZECH -> NLocale("cs", "CZ")
    LANG_ID_UK -> UK
    else -> UK
}
