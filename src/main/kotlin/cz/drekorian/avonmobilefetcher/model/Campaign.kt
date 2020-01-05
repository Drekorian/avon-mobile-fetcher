package cz.drekorian.avonmobilefetcher.model

/**
 * This data class stores the campaign data.
 *
 * @property year campaign year
 * @property id campaign identifier within the year
 */
data class Campaign(val year: String, val id: String) {

    companion object {
        private const val CATALOG_DELIMITER = " "
        private const val CAMPAIGN_DELIMITER = "/"

        /**
         * Returns a new [Catalog] instance from given main catalog name.
         *
         * @param mainCatalogName main catalog human readable name
         * @return new catalog instance from given main catalog name
         */
        fun fromMainCatalogName(mainCatalogName: String): Campaign {
            val rawCampaignId = mainCatalogName
                .split(CATALOG_DELIMITER)[1]
                .split(CAMPAIGN_DELIMITER)

            val year = rawCampaignId[0]
            val campaign = rawCampaignId[1]

            return Campaign(campaign, year)
        }
    }
}
