package cz.drekorian.avonmobilefetcher.multiplatform.util

/**
 * Locale representing English (United Kingdom).
 */
expect val UK: NLocale

/**
 * Returns user selected locale.
 *
 * @return user selected locale
 */
expect fun getDefaultLocale(): NLocale
