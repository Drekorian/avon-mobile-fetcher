package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.resources.i18n
import mu.KLogger

/**
 * Logs a debug to the [KLogger] instance, automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
fun KLogger.debugI18n(key: String, vararg args: Any) =
    debug { i18n(key).nFormat(*(args.map { arg -> arg.toString() }.toTypedArray())) }

/**
 * Logs an info to the [KLogger] instance, automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
fun KLogger.infoI18n(key: String, vararg args: Any) =
    info { i18n(key).nFormat(*(args.map { arg -> arg.toString() }.toTypedArray())) }

/**
 * Logs a warnings to the [KLogger] instance, automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
@Suppress("unused")
fun KLogger.warnI18n(key: String, vararg args: Any) =
    warn { i18n(key).nFormat(*(args.map { arg -> arg.toString() }.toTypedArray())) }

/**
 * Logs an error to the [KLogger] instance, automatically uses [i18n] and argument formatting.
 *
 * @param key I18n key
 * @param args formatting arguments
 */
fun KLogger.errorI18n(key: String, vararg args: Any) =
    error { i18n(key).nFormat(*(args.map { arg -> arg.toString() }.toTypedArray())) }

expect fun String.nFormat(vararg args: String): String
