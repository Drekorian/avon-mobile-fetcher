package cz.drekorian.avonmobilefetcher.flow.linenumber

import cz.drekorian.avonmobilefetcher.debugI18n
import cz.drekorian.avonmobilefetcher.errorI18n
import cz.drekorian.avonmobilefetcher.logger
import cz.drekorian.avonmobilefetcher.multiplatform.CFile
import cz.drekorian.avonmobilefetcher.nFormat
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toKString
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen

actual class LineNumberReader {

    companion object {

        private const val BUFFER_SIZE = 16
    }

    actual fun readLineNumbers(filePath: String): Map<Int, Int> {
        val file: CFile = fopen(filePath, "r") ?: run {
            logger.errorI18n("failed_to_open_file", filePath)
            return emptyMap()
        }

        val result = buildMap {
            memScoped {
                val line = allocArray<ByteVar>(BUFFER_SIZE)
                while (fgets(line, (BUFFER_SIZE * sizeOf<ByteVar>()).toInt(), file) != null) {
                    val split = line.toKString().split(" ")
                    val code = split[0].toInt()
                    val controlDigit = split[1].trimEnd().toInt()
                    put(code, controlDigit)
                }
            }
        }

        fclose(file)
        logger.debugI18n("loaded_line_numbers".nFormat(result.size.toString()))
        return result
    }
}
