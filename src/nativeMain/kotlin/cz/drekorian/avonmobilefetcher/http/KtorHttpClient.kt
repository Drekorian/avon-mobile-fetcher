package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.curl.Curl
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * This class serves as a simple HTTP client that automatically logs requests and responses.
 *
 * @author Marek Osvald
 */
actual object KtorHttpClient {

    internal actual val cookieJar: CookieJar = CookieJar()

    internal actual val client = HttpClient(Curl) {
        install(Logging) {
            level = LogLevel.BODY
            logger = HttpLogger
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    useAlternativeNames = true
                }
            )
        }
        install(HttpCookies) {
            storage = cookieJar
        }
        install(HttpRequestRetry) {
            maxRetries = 5
            retryIf { _, response -> !response.status.isSuccess() }
            retryOnException()
            delayMillis { retry -> retry * 1_000L } // will retry in 1, 2, 3, ... seconds
        }
    }

    /**
     * Sends a new HTTP GET request
     *
     * @param url request URL
     * @param headers request headers
     * @param params request query params
     */
    actual suspend fun get(
        url: String,
        headers: Map<String, String?>,
        params: Map<String, String>
    ): HttpResponse = client.get {
        url(url)
        headers.entries.forEach { (key, value) ->
            header(key, value)
        }
        params.entries.forEach { (key, value) ->
            parameter(key, value)
        }
    }

    actual suspend fun post(
        url: String,
        body: Any,
        headers: Map<String, String?>,
        params: Map<String, String?>,
    ): HttpResponse = client.post {
        url(url)
        contentType(ContentType.Application.Json)
        headers.entries.forEach { (key, value) ->
            header(key, value)
        }
        params.entries.forEach { (key, value) ->
            parameter(key, value)
        }
        setBody(body)
    }
}
