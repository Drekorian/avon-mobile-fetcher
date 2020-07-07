@file:JvmName("Main")

package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.flow.MasterFlow
import cz.drekorian.avonmobilefetcher.model.Campaign
import mu.KLogger
import mu.KotlinLogging
import java.util.Locale
import java.util.ResourceBundle

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
    logger = KotlinLogging.logger("main")
    I18n.resourceBundle = ResourceBundle.getBundle(I18N_RESOURCE_BUNDLE, locale)
    logger.infoI18n("welcome", "1.0.2")

    // process optional campaign name override
    when {
        args.isNotEmpty() -> {
            val override = args[0]
            logger.infoI18n("override_on", override)
            Campaign.processOverride(override)
        }
        else -> logger.infoI18n("override_off")
    }

    MasterFlow().execute()
}
