package cz.drekorian.avonmobilefetcher

import dev.mokkery.MockMode
import dev.mokkery.mock
import kotlin.test.BeforeTest

abstract class CommonTest {

    @BeforeTest
    fun before() {
        logger = mock(MockMode.autoUnit)
    }
}
