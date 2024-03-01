package cz.drekorian.avonmobilefetcher.multiplatform.util

/**
 * Locale representing English (United Kingdom).
 *
 * @author Marek Osvald
 */
expect val localeUk: NLocale

/**
 * Returns user selected locale.
 *
 * @return user selected locale
 * @author Marek Osvald
 */
expect val defaultLocale: NLocale

/**
 * This data class represents the selected user locale.
 *
 * @property languageCode ISO language code of the selected locale (e.g., "en" for English)
 * @property region ISO region code of the selected locale (e.g., "GB" for the United Kingdom)
 *
 * @author Marek Osvald
 */
data class NLocale(
    val languageCode: String,
    val region: String,
)
