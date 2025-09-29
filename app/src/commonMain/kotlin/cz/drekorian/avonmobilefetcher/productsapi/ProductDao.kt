package cz.drekorian.avonmobilefetcher.productsapi

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
internal data class ProductDao(
    val sku: String,
    @JsonNames("concept_code") val conceptCode: String? = null,
    val fsc: String,
    val title: String,
    val description: String,
    val price: Double,
    @JsonNames("price_standard") val priceStandard: Double? = null,
    @JsonNames("product_order") val productOrder: String?,
    @JsonNames("has_perfect_corp") val hasPerfectCorp: String? = null,
    @JsonNames("has_perfect_corp_model") val hasPerfectCorpModel: String? = null,
    @JsonNames("perfect_corp_patterns") val perfectCorpPatterns: String? = null,
    @JsonNames("perfect_corp_patterns_text") val perfectCorpPatternsText: String? = null,
    val publications: List<PublicationDao>? = null,
)
