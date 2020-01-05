package cz.drekorian.avonmobilefetcher.http

internal const val BASE_URL = "https://cz.avon-brochure.com"

private const val QUERY_KEY_CAMPAIGN_NUMBER = "campaignNumber"
private const val QUERY_KEY_LANGUAGE = "language"
private const val QUERY_KEY_MARKET = "market"
private const val QUERY_VALUE_LANGUAGE = "cs"
private const val QUERY_VALUE_MARKET = "CZ"

const val OK = 200
const val INDENT_FACTOR = 4

fun baseParams(campaignNumber: String): Map<String, String> = mapOf(
    QUERY_KEY_CAMPAIGN_NUMBER to campaignNumber,
    QUERY_KEY_LANGUAGE to QUERY_VALUE_LANGUAGE,
    QUERY_KEY_MARKET to QUERY_VALUE_MARKET
)
