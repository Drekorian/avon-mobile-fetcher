package cz.drekorian.avonmobilefetcher.http.login

import cz.drekorian.avonmobilefetcher.model.Login
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class LoginResponse(
    @JsonNames("loginRes") val loginResult: Login,
)
