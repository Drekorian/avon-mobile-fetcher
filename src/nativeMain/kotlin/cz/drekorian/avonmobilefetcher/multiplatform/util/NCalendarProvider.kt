package cz.drekorian.avonmobilefetcher.multiplatform.util

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toKString
import platform.posix.ctime_s
import platform.posix.time

actual object NCalendarProvider {

    private const val INDEX_MONTH = 1
    private const val INDEX_YEAR = 4
    private const val FORMATTED_LENGTH = 26

    private val months = mapOf(
        "Jan" to 1,
        "Feb" to 2,
        "Mar" to 3,
        "Apr" to 4,
        "May" to 5,
        "Jun" to 6,
        "Jul" to 7,
        "Aug" to 8,
        "Sep" to 9,
        "Oct" to 10,
        "Nov" to 11,
        "Dec" to 12,
    )

    actual fun getInstance(): NCalendar {
        val time = time(null)

        lateinit var formattedTime: String
        memScoped {
            val buffer = allocArray<ByteVar>(FORMATTED_LENGTH)
            ctime_s(buffer, (FORMATTED_LENGTH * sizeOf<ByteVar>()).toULong(), cValuesOf(time))
            formattedTime = buffer.toKString().trimEnd()
        }

        val splits = formattedTime.split(" ")
        if (splits.lastIndex < maxOf(INDEX_MONTH, INDEX_YEAR)) {
            throw IllegalStateException("Unable to parse calendar data from formatted time: $formattedTime")
        }

        return NCalendar(
            year = splits[INDEX_YEAR].trimEnd().toInt(),
            month = months[splits[INDEX_MONTH]]!!,
        )
    }
}
