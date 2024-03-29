package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.multiplatform.util.getCalendarInstance
import cz.drekorian.avonmobilefetcher.resources.i18n

/**
 * This data class stores the campaign data.
 *
 * @property year campaign year
 * @property id campaign identifier within the year
 * @author Marek Osvald
 */
data class Campaign(val year: String, val id: String) {

    companion object {
        val CAMPAIGN_OVERRIDE_REGEX = """[1-9]\d{3}\d[1-9]""".toRegex()

        private var override: String? = null

        /**
         * Loads given string as campaign override if possible.
         *
         * @param input override input
         */
        fun processOverride(input: String) {
            if (!CAMPAIGN_OVERRIDE_REGEX.matches(input)) {
                logger.error { i18n("invalid_campaign_override") }
                return
            }

            override = input
        }

        /**
         * Resets stored campaign override.
         */
        fun resetOverride() {
            override = null
        }

        /**
         * Returns current [Campaign] instance.
         *
         * @return current campaign instance
         */
        fun getCurrentCampaign(): Campaign = when (val catalogOverride = override) {
            null -> getCampaignNameFromCurrentDate()
            else -> getCampaignNameFromOverride(catalogOverride)
        }

        private fun getCampaignNameFromOverride(override: String): Campaign {
            val (year, id) = override.chunked(4)
            return Campaign(year, id)
        }

        private fun getCampaignNameFromCurrentDate(): Campaign {
            val calendar = getCalendarInstance()
            return Campaign(
                year = calendar.year.toString(),
                id = calendar.month.toString().padStart(2, '0')
            )
        }
    }

    /**
     * Returns campaign as a Restful argument for HTTP API calls.
     */
    fun toRestfulArgument(): String = "c${id}_cz_$year"
}
