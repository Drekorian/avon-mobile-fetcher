package cz.drekorian.avonmobilefetcher.domain

import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.multiplatform.util.getCalendarInstance

/**
 * This repository provides access to the current [Campaign] instance.
 *
 * @param runtimeOverride current runtime override
 * @author Marek Osvald
 */
internal class CampaignRepository(
    private val runtimeOverride: RuntimeOverride,
) {
    /**
     * Returns current [Campaign] instance.
     *
     * @return current campaign instance
     */
    fun getCurrentCampaign(): Campaign {
        return if (runtimeOverride.hasCampaign) {
            getCampaignNameFromOverride(runtimeOverride.campaign)
        } else {
            getCampaignNameFromCurrentDate()
        }
    }

    private fun getCampaignNameFromOverride(override: String): Campaign {
        val (year, id) = override.chunked(4)
        return Campaign(year, id)
    }

    private fun getCampaignNameFromCurrentDate(): Campaign = with(getCalendarInstance()) {
        Campaign(
            year = year.toString(),
            id = month.toString().padStart(2, '0')
        )
    }
}
