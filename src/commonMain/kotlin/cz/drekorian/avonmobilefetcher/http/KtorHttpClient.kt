package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * This class serves as a simple HTTP client that automatically logs requests and responses.
 *
 * @author Marek Osvald
 */
internal object KtorHttpClient {

    private val client: HttpClient by lazy {
        HttpClient(engineConfig) {
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
        }
    }

    /**
     * Sends a new HTTP GET request
     *
     * @param builder [HttpRequestBuilder] function.
     */
    suspend fun get(builder: HttpRequestBuilder.() -> Unit): HttpResponse = client.get { builder.invoke(this) }
}

internal expect val engineConfig: HttpClientEngineFactory<HttpClientEngineConfig>
