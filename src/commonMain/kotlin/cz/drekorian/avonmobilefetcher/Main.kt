package cz.drekorian.avonmobilefetcher

import mu.KLogger

/** Main application logger **/
lateinit var logger: KLogger

fun main(args: Array<String>) {
    Application().start(args)
}

/**
 * Enables debug-level logs.
 */
internal expect fun enableDebugLogging()
