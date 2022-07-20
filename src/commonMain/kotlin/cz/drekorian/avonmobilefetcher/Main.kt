package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.flow.MasterFlow
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsOverride
import cz.drekorian.avonmobilefetcher.model.Campaign
import mu.KLogger
import mu.KotlinLogging

private const val APP_VERSION = "2.1.0"
private const val ARGUMENT_KEY_DEBUG = "debug"
private const val ARGUMENTS_DELIMITER = '='
private const val CATALOG_DELIMITER = ','

internal val ARGUMENT_CATALOGS = """--catalogs=[a-zA-Z]+[,a-zA-Z\-+]*""".toRegex()

/** Main application logger **/
lateinit var logger: KLogger

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

    processCatalogsOverride(distinctArgs)

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

internal fun processCatalogsOverride(distinctArgs: MutableSet<String>) {
    distinctArgs.firstOrNull { arg -> ARGUMENT_CATALOGS.matches(arg) }?.let { arg ->
        val catalogsOverride = arg.split(ARGUMENTS_DELIMITER)[1].split(CATALOG_DELIMITER).toList()
        CatalogsOverride.setCatalogs(catalogsOverride)
        distinctArgs -= arg
    }
}

private fun createLogger() {
    logger = KotlinLogging.logger("main")
}

/**
 * Enables debug-level logs.
 */
expect fun enableDebugLogging()
