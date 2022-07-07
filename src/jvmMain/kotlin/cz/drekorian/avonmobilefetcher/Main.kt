@file:JvmName("MainJvm")

package cz.drekorian.avonmobilefetcher

import org.slf4j.impl.SimpleLogger

actual fun enableDebugLogging() {
    System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
}
