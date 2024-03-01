package cz.drekorian.avonmobilefetcher.http

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

internal actual val engineConfig: HttpClientEngineFactory<HttpClientEngineConfig> = Darwin
