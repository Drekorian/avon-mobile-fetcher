package cz.drekorian.avonmobilefetcher.http

import cz.drekorian.avonmobilefetcher.logger
import io.ktor.client.plugins.logging.Logger
import kotlin.native.concurrent.ThreadLocal

object HttpLogger : Logger {
    override fun log(message: String) {
        logger.debug { message }
    }
}
