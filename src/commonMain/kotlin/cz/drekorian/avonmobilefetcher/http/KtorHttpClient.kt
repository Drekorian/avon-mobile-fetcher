package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

/**
 * This class serves as a simple HTTP client that automatically logs requests and responses.
 *
 * @author Marek Osvald
 */
expect object KtorHttpClient {

    internal val client: HttpClient

    /**
     * Sends a new HTTP GET request
     *
     * @param url request URL
     * @param headers request headers
     * @param params request query params
     */
    suspend fun get(
        url: String,
        headers: Map<String, String?> = mapOf(),
        params: Map<String, String> = mapOf(),
    ): HttpResponse
}
