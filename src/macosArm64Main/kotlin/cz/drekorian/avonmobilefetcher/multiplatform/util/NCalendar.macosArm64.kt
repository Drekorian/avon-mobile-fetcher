package cz.drekorian.avonmobilefetcher.multiplatform.util

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.ctime_r
import platform.posix.time

private const val INDEX_MONTH = 1
private const val FORMATTED_LENGTH = 26

@OptIn(ExperimentalForeignApi::class)
actual fun getCalendarInstance(): NCalendar {
    val time = time(null)

    lateinit var formattedTime: String
    memScoped {
        val buffer = allocArray<ByteVar>(FORMATTED_LENGTH)
        ctime_r(cValuesOf(time), buffer)
        formattedTime = buffer.toKString().trimEnd()
    }

    val splits = formattedTime.split(" ")
    if (splits.lastIndex < maxOf(INDEX_MONTH, splits.lastIndex)) {
        throw IllegalStateException("Unable to parse calendar data from formatted time: $formattedTime")
    }

    return NCalendar(
        year = splits[splits.lastIndex].trimEnd().toInt(),
        month = months[splits[INDEX_MONTH]]!!,
    )
}
