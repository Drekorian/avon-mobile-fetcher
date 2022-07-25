package cz.drekorian.avonmobilefetcher.http.validate

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody(
    val userId: String,
    val password: String,
)
