package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual object KtorHttpClient {
    internal actual val client = HttpClient(CIO) {
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
}
