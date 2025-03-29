@file:JvmName("MainJvm")

package cz.drekorian.avonmobilefetcher

import org.slf4j.simple.SimpleLogger

internal actual fun enableDebugLogging() {
    System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
}
