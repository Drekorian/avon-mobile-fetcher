package cz.drekorian.avonmobilefetcher.http

import khttp.responses.Response
import org.slf4j.Logger

abstract class Request {
    protected fun checkStatusCode(response: Response, logger: Logger, errorMessage: String): Boolean {
        val statusCode = response.statusCode
        if (statusCode != OK) {
            logger.error("$errorMessage ($statusCode) ${response.text}")
            return false
        }

        return true
    }
}
