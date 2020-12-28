@file:JvmName("Main")

package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.flow.MasterFlow
import cz.drekorian.avonmobilefetcher.model.Campaign
import mu.KLogger
import mu.KotlinLogging
import org.slf4j.impl.SimpleLogger
import java.lang.System.setProperty
import java.util.Locale
import java.util.ResourceBundle

private const val APP_VERSION = "1.4.0"
private const val ARGUMENT_KEY_DEBUG = "debug"

/** I18n resource bundle **/
private const val I18N_RESOURCE_BUNDLE = "locale"

private val SUPPORTED_LOCALES = setOf(Locale("cs", "CZ"), Locale.UK)

/** Main application logger **/
lateinit var logger: KLogger

/** Retrieves supported locale based on user's locale. Default is en-GB **/
private val locale: Locale
    get() {
        val userLocale = Locale.getDefault()
        if (userLocale in SUPPORTED_LOCALES) {
            return userLocale
        }

        // use en-GB as default for unsupported locales
        return Locale.UK
    }

/**
 * Script entry point.
 *
 * @param args command-line arguments
 */
fun main(args: Array<String>) {
    // process optional arguments
    processArgs(args)
    MasterFlow().execute()
}

private fun processArgs(args: Array<String>) {
    val distinctArgs = args.toMutableSet()

    if (ARGUMENT_KEY_DEBUG in distinctArgs) {
        distinctArgs -= ARGUMENT_KEY_DEBUG
        enableDebugLogging()
    }

    createLogger()
    logger.infoI18n("welcome", APP_VERSION)

    when (val override = distinctArgs.firstOrNull { argument -> Campaign.CAMPAIGN_OVERRIDE_REGEX.matches(argument) }) {
        null -> logger.infoI18n("override_off")
        else -> {
            logger.infoI18n("override_on", override)
            distinctArgs -= override
            Campaign.processOverride(override)
        }
    }

    if (distinctArgs.isNotEmpty()) {
        logger.errorI18n("unknown_arguments", distinctArgs.joinToString(separator = ", "))
    }
}

private fun createLogger() {
    logger = KotlinLogging.logger("main")
    I18n.resourceBundle = ResourceBundle.getBundle(I18N_RESOURCE_BUNDLE, locale)
}

private fun enableDebugLogging() {
    setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
}
