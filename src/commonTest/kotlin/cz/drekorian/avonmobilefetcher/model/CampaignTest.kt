package cz.drekorian.avonmobilefetcher.model

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A test class for [Campaign].
 *
 * @author Marek Osvald
 */
internal class CampaignTest {

    @Test
    fun `should successfully convert to a restful argument`() {
        val campaign = Campaign(year = "2025", id = "04")

        assertEquals(
            campaign.toRestfulArgument(),
            "c04_cz_2025",
        )
    }
}
