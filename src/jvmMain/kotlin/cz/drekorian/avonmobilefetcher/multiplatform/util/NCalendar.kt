package cz.drekorian.avonmobilefetcher.multiplatform.util

import java.util.Calendar

actual object NCalendarProvider {

    actual fun getInstance(): NCalendar {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1
        return NCalendar(
            year = year,
            month = month,
        )
    }
}
