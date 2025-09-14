package cz.drekorian.avonmobilefetcher

import mu.KotlinLoggingConfiguration
import mu.KotlinLoggingLevel

internal actual fun enableDebugLogging() {
    KotlinLoggingConfiguration.logLevel = KotlinLoggingLevel.DEBUG
}
