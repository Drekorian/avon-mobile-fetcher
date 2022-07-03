/**
 * This file contains various extensions.
 *
 * @author Marek Osvald
 */
package cz.drekorian.avonmobilefetcher

import mu.KLogger

/**
 * Logs a debug to [logger], automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
fun KLogger.debugI18n(key: String, vararg args: Any) = debug(i18n(key).format(*args))

/**
 * Logs an info to [logger], automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
fun KLogger.infoI18n(key: String, vararg args: Any) = info(i18n(key).format(*args))

/**
 * Logs a warnings to [logger], automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
@Suppress("unused")
fun KLogger.warnI18n(key: String, vararg args: Any) = warn(i18n(key).format(*args))

/**
 * Logs an error to [logger], automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
fun KLogger.errorI18n(key: String, vararg args: Any) = error(i18n(key).format(*args))
