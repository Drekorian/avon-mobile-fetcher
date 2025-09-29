package cz.drekorian.avonmobilefetcher.productsapi

import cz.drekorian.avonmobilefetcher.http.HttpLogger
import cz.drekorian.avonmobilefetcher.http.engineConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val productsApiModule = module(createdAtStart = false) {

    factoryOf(::GetProductsUseCase)
    factoryOf(::GetProductPagesUseCase)

}

val httpClient by lazy {
    HttpClient(engineConfig) {
        install(Logging) {
            level = LogLevel.ALL
            logger = HttpLogger
        }
        install(Auth)
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
