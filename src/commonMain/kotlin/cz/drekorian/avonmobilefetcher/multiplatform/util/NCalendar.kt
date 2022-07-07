package cz.drekorian.avonmobilefetcher.multiplatform.util

data class NCalendar(
    val year: Int,
    val month: Int,
)

expect object NCalendarProvider {

    fun getInstance(): NCalendar
}
