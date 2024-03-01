package cz.drekorian.avonmobilefetcher.multiplatform.util

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toKString
import platform.posix.ctime_s
import platform.posix.time

private const val INDEX_MONTH = 1
private const val INDEX_YEAR = 4
private const val FORMATTED_LENGTH = 26

@OptIn(ExperimentalForeignApi::class)
actual fun getCalendarInstance(): NCalendar {
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
