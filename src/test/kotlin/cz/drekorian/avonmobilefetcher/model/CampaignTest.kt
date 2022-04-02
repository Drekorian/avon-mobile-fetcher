package cz.drekorian.avonmobilefetcher.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * A test class for [Campaign].
 *
 * @author Marek Osvald
 */
class CampaignTest {

    @BeforeEach
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
        assertThat(campaign.year).isEqualTo("2020")
        assertThat(campaign.id).isEqualTo("01")
    }
}
