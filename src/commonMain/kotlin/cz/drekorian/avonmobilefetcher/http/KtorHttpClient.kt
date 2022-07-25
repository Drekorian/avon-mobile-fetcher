package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent

expect object KtorHttpClient {

    internal val client: HttpClient

    internal val cookieJar: CookieJar

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

    suspend fun post(
        url: String,
        body: Any = EmptyContent,
        headers: Map<String, String?> = mapOf(),
        params: Map<String, String?> = mapOf(),
    ): HttpResponse
}
