package cz.drekorian.avonmobilefetcher

import java.util.ResourceBundle

/**
 * This object handles the I18n of error messages.
 *
 * @author Marek Osvald
 */
object I18n {
    lateinit var resourceBundle: ResourceBundle

    /**
     * Returns the localised string value for given key.
     *
     * @param key I18n key to retrieve
     * @return internationalized string from a resource bundle
     */
    fun get(key: String) = resourceBundle.getString(key) ?: "key_not_found<$key>"
}

/**
 * A convenience method for [I18n.get].
 *
 * @param key I18n key to retrieve
 * @return internationalized string from a resource bundle
 */
fun i18n(key: String) = I18n.get(key)
