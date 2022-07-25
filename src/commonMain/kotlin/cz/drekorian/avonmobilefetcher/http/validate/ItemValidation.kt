package cz.drekorian.avonmobilefetcher.http.validate

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ItemValidation(
    @JsonNames("lineNr") val lineNumber: String,
    @JsonNames("prodNm") val productName: String = "",
    @JsonNames("rsnCd") val reasonCode: String = "",
    val valid: Boolean,
)
