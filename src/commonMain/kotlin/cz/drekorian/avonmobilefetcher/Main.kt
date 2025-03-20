package cz.drekorian.avonmobilefetcher

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.varargValues
import cz.drekorian.avonmobilefetcher.flow.MasterFlow
import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsOverride
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.multiplatform.util.getFetcherCommandName
import cz.drekorian.avonmobilefetcher.resources.i18n
import mu.KLogger
import mu.KotlinLogging

/** Main application logger **/
lateinit var logger: KLogger

/**
 * Script entry point.
 *
 * @param args command-line arguments
 */
fun main(args: Array<String>) = FetcherCommand().main(args)

internal class FetcherCommand : CliktCommand(name = getFetcherCommandName()) {
    private val isDebug by option("-d", "--debug", help = i18n("help_option_debug")).flag()
    private val campaign by option("-c", "--campaign", help = i18n("help_option_campaign")).default("")
    private val catalogs by option("-a", "--catalogs", help = i18n("help_option_catalogs"))
        .varargValues()
        .default(emptyList())

    init {
        context { localization = FetcherLocalization() }
    }

    override fun run() {
        if (isDebug) {
            enableDebugLogging()
        }
        createLogger()

        if (campaign.matches(Campaign.CAMPAIGN_OVERRIDE_REGEX)) {
            logger.infoI18n("override_on", campaign)
            Campaign.processOverride(campaign)
        } else {
            logger.infoI18n("override_off")
        }

        if (catalogs.isNotEmpty()) {
            CatalogsOverride.setCatalogs(catalogs)
        }

        logger.infoI18n("welcome", BuildConfig.appVersion)

        MasterFlow().execute()
    }
}

private fun createLogger() {
    logger = KotlinLogging.logger("main")
}

/**
 * Enables debug-level logs.
 */
expect fun enableDebugLogging()
