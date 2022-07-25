package cz.drekorian.avonmobilefetcher.flow.linenumber

import cz.drekorian.avonmobilefetcher.debugI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.nFormat
import java.io.File

actual class LineNumberReader {

    actual fun readLineNumbers(filePath: String): Map<Int, Int> {
        val result = try {
            File(filePath).reader().use { reader ->
                reader.readLines().map { line ->
                    val split = line.split(" ")
                    val code = split[0].toInt()
                    val controlDigit = split[1].toInt()
                    code to controlDigit
                }.toMap()
            }
        } catch (e: Exception) {
            emptyMap()
        }

        logger.debugI18n("loaded_line_numbers".nFormat(result.size.toString()))
        return result
    }
}
