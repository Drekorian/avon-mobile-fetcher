package cz.drekorian.avonmobilefetcher.productsapi

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
internal data class GetProductsRequest(
    val index: String,
    val environment: String,
    val client: String,
    @JsonNames("client_storage") val clientStorage: String,
    val publication: String,
    val columns: List<String>,
)
