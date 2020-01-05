package cz.drekorian.avonmobilefetcher

import java.util.*

object I18n {
    lateinit var resourceBundle: ResourceBundle

    /**
     * Returns the localised string value for given key.
     */
    fun get(key: String) : String = resourceBundle.getString(key) ?: "key_not_found<$key>"
}
