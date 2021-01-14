package cz.drekorian.avonmobilefetcher.model

import cz.drekorian.avonmobilefetcher.flow.catalog.CatalogsOverride
import cz.drekorian.avonmobilefetcher.i18n
import cz.drekorian.avonmobilefetcher.logger
import java.lang.IllegalArgumentException
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
         * Returns a new [Catalog] instance from given main catalog name.
         *
         * @param mainCatalogName main catalog human readable name
         * @return new catalog instance from given main catalog name
         */
        fun getCurrentCampaign(mainCatalogName: String): Campaign {
            return when {
                override != null -> getCampaignNameFromOverride(override!!)
                isOriginalSlashedFormat(mainCatalogName) -> getCampaignNameFromOriginalSlashedFormat(mainCatalogName)
                isNewFormat(mainCatalogName) -> getCampaignNameFromNewFormat(mainCatalogName)
                isNamelessFormat(mainCatalogName) -> getCampaignNameFromNamelessFormat()
                else -> throw IllegalArgumentException(i18n("unknown_campaign_name"))
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

        private fun isNamelessFormat(input: String) = input == NAMELESS_CATALOG_NAME || CatalogsOverride.hasOverride

        private fun getCampaignNameFromNamelessFormat(): Campaign {
            val calendar = Calendar.getInstance()
            return Campaign(calendar[Calendar.YEAR].toString(), (calendar[Calendar.MONTH] + 1).toString())
        }
    }
}
