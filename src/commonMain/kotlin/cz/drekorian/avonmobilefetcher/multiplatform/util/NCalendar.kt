package cz.drekorian.avonmobilefetcher.multiplatform.util

/**
 * This data class represents simple calendar instance.
 *
 * @property year calendar year (e.g., 2022)
 * @property month calendar month (e.g., 1 for January, 12 for December)
 */
data class NCalendar(
    val year: Int,
    val month: Int,
)

/**
 * This class provides [NCalendar] instances.
 *
 * @author Marek Osvald
 */
expect object NCalendarProvider {

    /**
     * Returns a new [NCalendar] instance representing the current date.
     *
     * @return [NCalendar] instance representing the current date.
     */
    fun getInstance(): NCalendar
}
