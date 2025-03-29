package cz.drekorian.avonmobilefetcher.domain

import cz.drekorian.avonmobilefetcher.CommonTest
import cz.drekorian.avonmobilefetcher.OpenForTesting
import cz.drekorian.avonmobilefetcher.model.Campaign
import cz.drekorian.avonmobilefetcher.multiplatform.util.getCalendarInstance
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CampaignRepositoryTest : CommonTest() {

    private val runtimeOverride: RuntimeOverride = mock {
        every { campaign } returns ""
    }

    private fun repository(
        runtimeOverride: RuntimeOverride = this.runtimeOverride,
    ): CampaignRepository = CampaignRepository(
        runtimeOverride = runtimeOverride,
    )

    @Test
    fun `when override is not set it should return the current campaign instance`() {
        // given
        val calendar = getCalendarInstance()

        // when
        assertEquals(
            repository().getCurrentCampaign(),
            Campaign(
                year = calendar.year.toString(),
                id = buildString {
                    if (calendar.month < 10) {
                        append("0")
                    }
                    append(calendar.month)
                }
            ),
        )
    }

    @Test
    fun `should return the campaign from override when override is set`() {
        // given
        val campaign = "202503"
        every { runtimeOverride.campaign } returns campaign

        // when
        assertEquals(
            repository().getCurrentCampaign(),
            Campaign(year = "2025", id = "03"),
        )
    }
}