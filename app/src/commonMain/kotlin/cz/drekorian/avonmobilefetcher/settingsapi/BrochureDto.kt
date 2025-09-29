@file:OptIn(ExperimentalSerializationApi::class)

package cz.drekorian.avonmobilefetcher.settingsapi

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class BrochuresDto(
    val brochures: List<BrochureDto>,
)

@Serializable
data class BrochureDto(
    val id: Long,
    @JsonNames("external_id") val externalId: String,
    val infos: InfosDto,
)

@Serializable
data class InfosDto(
    val publication: PublicationDto,
)

@Serializable
data class PublicationDto(
    val options: OptionsDto,
)

@Serializable
data class OptionsDto(
    val modules: ModulesDto,
)

@Serializable
data class ModulesDto(
    val feed: FeedDto,
)

@Serializable
data class FeedDto(
    @Suppress("SpellCheckingInspection") @JsonNames("productfeed") val productFeed: String,
)
