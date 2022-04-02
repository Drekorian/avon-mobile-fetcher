package cz.drekorian.avonmobilefetcher.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.Calendar

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
        @Suppress("SpellCheckingInspection")
        val campaign = Campaign.getCurrentCampaign(emptyList())

        // assert
        assertThat(campaign.year).isEqualTo("2020")
        assertThat(campaign.id).isEqualTo("01")
    }

    @Suppress("SpellCheckingInspection")
    @ParameterizedTest
    @ValueSource(strings = ["Katalog 2020/01", "Katalog 2020/1", "Katalog 2020/10", "Katalog 1999/99"])
    fun `when no override with old format Campaign is properly parsed`(rawCampaign: String) {
        // act
        val campaign = Campaign.getCurrentCampaign(listOf(Catalog("katalog", rawCampaign)))

        // assert
        val (year, id) = rawCampaign.split(" ")[1].split("/")
        assertThat(campaign.year).isEqualTo(year)
        assertThat(campaign.id).isEqualTo(id)
    }

    @Suppress("SpellCheckingInspection")
    @ParameterizedTest
    @ValueSource(strings = ["Katalog 1", "Katalog 09", "Katalog 14", "Katalog 99"])
    fun `when no override with new format Campaign is properly parsed`(rawCampaign: String) {
        // act
        val campaign = Campaign.getCurrentCampaign(listOf(Catalog("katalog", rawCampaign)))

        // assert
        assertThat(campaign.year).isEqualTo(Calendar.getInstance()[Calendar.YEAR].toString())
        assertThat(campaign.id).isEqualTo(rawCampaign.split(' ')[1])
    }
}
