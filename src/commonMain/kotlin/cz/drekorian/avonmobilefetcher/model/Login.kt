package cz.drekorian.avonmobilefetcher.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Login constructor(
    @JsonNames("acctNr") val accountNumber: String,
)
