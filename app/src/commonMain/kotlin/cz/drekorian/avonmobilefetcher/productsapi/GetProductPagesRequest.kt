package cz.drekorian.avonmobilefetcher.productsapi

import kotlinx.serialization.Serializable

@Serializable
internal data class GetProductPagesRequest(
    val client: String,
    val environment: String,
    val publication: String,
    val skus: List<String>,
)
