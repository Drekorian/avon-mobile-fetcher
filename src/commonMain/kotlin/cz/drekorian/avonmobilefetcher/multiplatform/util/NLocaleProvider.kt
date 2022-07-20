package cz.drekorian.avonmobilefetcher.multiplatform.util

/**
 * This class provides [NLocale] instances.
 *
 * @author Marek Osvald
 */
expect object NLocaleProvider {

    /**
     * Locale representing English (United Kingdom).
     */
    val UK: NLocale

    /**
     * Returns user selected locale.
     *
     * @return user selected locale
     */
    fun getDefault(): NLocale
}
