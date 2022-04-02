package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.i18n
import cz.drekorian.avonmobilefetcher.logger
import java.util.Calendar

/**
 * This data class stores the campaign data.
 *
 * @property year campaign year
 * @property id campaign identifier within the year
 * @author Marek Osvald
 */
data class Campaign(val year: String, val id: String) {

    companion object {
        val CAMPAIGN_OVERRIDE_REGEX = "[1-9][0-9]{3}[0-9][1-9]".toRegex()

        @Suppress("SpellCheckingInspection")
        private val CAMPAIGN_SLASHED_FORMAT_REGEX = "Katalog ([1-9][0-9]{3})/([0-9]?[0-9])".toRegex()
        private const val SLASHED_FORMAT_REQUIRED_GROUPS_COUNT = 3

        @Suppress("SpellCheckingInspection")
        private val CAMPAIGN_NEW_FORMAT_REGEX = "Katalog ([0-9]?[0-9])".toRegex()
        private const val NEW_FORMAT_REQUIRED_GROUPS_COUNT = 2

        @Suppress("SpellCheckingInspection")
        private const val NAMELESS_CATALOG_NAME = "katalog"

        private var override: String? = null

        /**
         * Loads given string as campaign override if possible.
         *
         * @param input override input
         */
        fun processOverride(input: String) {
            if (!CAMPAIGN_OVERRIDE_REGEX.matches(input)) {
                logger.error(i18n("invalid_campaign_override"))
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
         * Returns a new [Campaign] instance from given list of catalogs.
         *
         * @param catalogs list of available catalogs
         * @return new campaign instance from the list of available catalogs
         */
        fun getCurrentCampaign(catalogs: List<Catalog>): Campaign {
            val mainCatalogName = catalogs.find { catalog -> catalog.id == NAMELESS_CATALOG_NAME }?.name ?: ""

            return when {
                override != null -> getCampaignNameFromOverride(override!!)
                isOriginalSlashedFormat(mainCatalogName) -> getCampaignNameFromOriginalSlashedFormat(mainCatalogName)
                isNewFormat(mainCatalogName) -> getCampaignNameFromNewFormat(mainCatalogName)
                else -> getCampaignNameFromCurrentDate()
            }
        }

        private fun getCampaignNameFromOverride(override: String): Campaign {
            val (year, id) = override.chunked(4)
            return Campaign(year, id)
        }

        private fun isOriginalSlashedFormat(input: String): Boolean {
            return CAMPAIGN_SLASHED_FORMAT_REGEX.find(input)?.groups?.size == SLASHED_FORMAT_REQUIRED_GROUPS_COUNT
        }

        private fun getCampaignNameFromOriginalSlashedFormat(input: String): Campaign {
            val groups = CAMPAIGN_SLASHED_FORMAT_REGEX.find(input)!!.groups
            return Campaign(year = groups[1]!!.value, id = groups[2]!!.value)
        }

        private fun isNewFormat(input: String): Boolean {
            return CAMPAIGN_NEW_FORMAT_REGEX.find(input)?.groups?.size == NEW_FORMAT_REQUIRED_GROUPS_COUNT
        }

        private fun getCampaignNameFromNewFormat(input: String): Campaign {
            val groups = CAMPAIGN_NEW_FORMAT_REGEX.find(input)!!.groups
            val year = Calendar.getInstance()[Calendar.YEAR].toString()
            return Campaign(year = year, id = groups[1]!!.value)
        }

        private fun getCampaignNameFromCurrentDate(): Campaign {
            val calendar = Calendar.getInstance()
            return Campaign(calendar[Calendar.YEAR].toString(), "%02d".format(calendar[Calendar.MONTH] + 1))
        }
    }

    /**
     * Returns campaign as a RESTful argument for HTTP API calls.
     */
    fun toRestfulArgument(): String = "c${id}_cz_$year"
}
