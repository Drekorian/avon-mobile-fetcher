package cz.drekorian.avonmobilefetcher.productsapi

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PublicationDao constructor(
    @JsonNames("publication_uid") val publicationUid: String,
    @JsonNames("page_uuid") val page: Int,
)
