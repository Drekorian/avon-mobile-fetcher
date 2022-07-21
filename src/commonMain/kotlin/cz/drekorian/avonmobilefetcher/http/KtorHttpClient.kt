package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
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
     * @param builder [HttpRequestBuilder] function.
     */
    suspend fun get(
        builder: HttpRequestBuilder.() -> Unit,
    ): HttpResponse
}
