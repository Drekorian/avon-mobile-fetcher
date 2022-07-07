package cz.drekorian.avonmobilefetcher.model

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A test class for [Campaign].
 *
 * @author Marek Osvald
 */
class CampaignTest {

    @BeforeTest
    fun setUp() {
        Campaign.resetOverride()
    }

    @Test
    fun `when valid override is in place Campaign is properly parsed`() {
        // arrange
        Campaign.processOverride("202001")

        // act
        val campaign = Campaign.getCurrentCampaign()

        // assert
        assertEquals("2020", campaign.year)
        assertEquals("01", campaign.id)
    }
}
