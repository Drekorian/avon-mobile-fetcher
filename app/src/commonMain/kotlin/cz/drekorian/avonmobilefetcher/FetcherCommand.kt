package cz.drekorian.avonmobilefetcher

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.varargValues
import cz.drekorian.avonmobilefetcher.domain.RuntimeOverride
import cz.drekorian.avonmobilefetcher.flow.MasterFlow
import cz.drekorian.avonmobilefetcher.multiplatform.util.getFetcherCommandName
import cz.drekorian.avonmobilefetcher.resources.i18n
import mu.KotlinLogging

internal class FetcherCommand(
    private val masterFlow: MasterFlow,
    private val runtimeOverride: RuntimeOverride,
) : CliktCommand(name = getFetcherCommandName()) {
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

        runtimeOverride.campaign = campaign
        runtimeOverride.catalogs = catalogs

        logger.infoI18n("welcome", BuildConfig.appVersion)

        masterFlow.execute()
    }
}

private fun createLogger() {
    logger = KotlinLogging.logger("main")
}
