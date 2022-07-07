package cz.drekorian.avonmobilefetcher.multiplatform.util

expect object NLocaleProvider {

    val UK: NLocale

    fun getDefault(): NLocale
}
