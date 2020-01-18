package cz.drekorian.avonmobilefetcher.model

import java.util.*

/**
 * This data class stores the campaign data.
 *
 * @property year campaign year
 * @property id campaign identifier within the year
 */
data class Campaign(val year: String, val id: String) {

    companion object {
        private const val CATALOG_DELIMITER = " "
//        private const val CAMPAIGN_DELIMITER = "/"

        /**
         * Returns a new [Catalog] instance from given main catalog name.
         *
         * @param mainCatalogName main catalog human readable name
         * @return new catalog instance from given main catalog name
         */
        fun fromMainCatalogName(mainCatalogName: String): Campaign {
// FIXME: Since campaign 2020/02 the main catalog name is no longer "Katalog YYYY/CC" but just "Katalog [C]C"
//            val rawCampaignId = mainCatalogName
//                .split(CATALOG_DELIMITER)[1]
//                .split(CAMPAIGN_DELIMITER)

//            val year = rawCampaignId[0]
//            val campaign = rawCampaignId[1]
            val year = Calendar.getInstance()[Calendar.YEAR].toString()
            val campaign = mainCatalogName.split(CATALOG_DELIMITER)[1]

            return Campaign(year, campaign)
        }
    }
}
