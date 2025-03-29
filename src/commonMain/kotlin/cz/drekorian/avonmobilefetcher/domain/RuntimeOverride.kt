package cz.drekorian.avonmobilefetcher.domain

import cz.drekorian.avonmobilefetcher.OpenForTesting
import cz.drekorian.avonmobilefetcher.errorI18n
import cz.drekorian.avonmobilefetcher.infoI18n
import cz.drekorian.avonmobilefetcher.logger

private val CampaignOverrideRegex = """[1-9]\d{3}\d[1-9]""".toRegex()

@OpenForTesting
internal class RuntimeOverride {

    var campaign: String = ""
        set(value) {
            if (value.isEmpty()) {
                logger.infoI18n("override_off")
                field = value
                return
            }

            if (!CampaignOverrideRegex.matches(value)) {
                logger.errorI18n("invalid_campaign_override")
                return
            }

            logger.infoI18n("override_on", value)
            field = value
        }

    var catalogs: List<String> = emptyList()
}

internal val RuntimeOverride.hasCampaign: Boolean
    get() = campaign.isNotEmpty()

internal val RuntimeOverride.hasCatalogs: Boolean
    get() = catalogs.isNotEmpty()
