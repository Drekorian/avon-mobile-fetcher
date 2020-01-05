package cz.drekorian.avonmobilefetcher.http

import cz.drekorian.avonmobilefetcher.logger
import khttp.DEFAULT_TIMEOUT
import khttp.responses.Response
import khttp.structures.authorization.Authorization
import khttp.structures.files.FileLike

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
        logger.info("Request: ${response.request.url}")

        when (response.text[0]) {
            '[' -> logger.debug("Response:\n${response.jsonArray.toString(INDENT_FACTOR)}")
            '{' -> logger.debug("Response:\n${response.jsonObject.toString(INDENT_FACTOR)}")
            else -> logger.debug("Response:\n${response.text}")
        }

        return response
    }
}
