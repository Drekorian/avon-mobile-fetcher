package cz.drekorian.avonmobilefetcher.http

import cz.drekorian.avonmobilefetcher.debugI18n
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger
import khttp.DEFAULT_TIMEOUT
import khttp.responses.Response
import khttp.structures.authorization.Authorization
import khttp.structures.files.FileLike

/**
 * This class serves as a simple HTTP client that automatically logs requests and responses.
 *
 * @author Marek Osvald
 */
object KHttpClient {

    /**
     * Sends a new HTTP GET request
     *
     * @param url request URL
     * @param headers request headers
     * @param params request query params
     */
    fun get(
        url: String,
        headers: Map<String, String?> = mapOf(),
        params: Map<String, String> = mapOf(),
        data: Any? = null,
        json: Any? = null,
        auth: Authorization? = null,
        cookies: Map<String, String>? = null,
        timeout: Double = DEFAULT_TIMEOUT,
        allowRedirects: Boolean? = null,
        stream: Boolean = false,
        files: List<FileLike> = listOf()
    ): Response {
        val response: Response =
            khttp.get(url, headers, params, data, json, auth, cookies, timeout, allowRedirects, stream, files)
        logger.infoI18n("request", response.request.url)

        val prettyResponse = when (response.text[0]) {
            '[' -> response.jsonArray.toString(INDENT_FACTOR)
            '{' -> response.jsonObject.toString(INDENT_FACTOR)
            else -> response.text
        }

        logger.debugI18n("response", prettyResponse)
        return response
    }
}
