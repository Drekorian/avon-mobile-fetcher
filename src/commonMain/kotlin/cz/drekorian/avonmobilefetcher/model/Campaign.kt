package cz.drekorian.avonmobilefetcher.model

/**
 * This data class stores the campaign data.
 *
 * @property year campaign year
 * @property id campaign identifier within the year
 * @author Marek Osvald
 */
data class Campaign(val year: String, val id: String) {

    /**
     * Returns campaign as a Restful argument for HTTP API calls.
     */
    fun toRestfulArgument(): String = "c${id}_cz_$year"
}
