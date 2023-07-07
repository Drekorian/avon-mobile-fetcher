package cz.drekorian.avonmobilefetcher

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.FILE
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fprintf
import platform.posix.fwrite

@OptIn(ExperimentalForeignApi::class)
private typealias CFile = CPointer<FILE>

@OptIn(ExperimentalForeignApi::class)
actual object CsvPrinter {

    actual fun print(filePath: String, data: Iterable<String>, vararg header: String) {
        val file: CFile = fopen(filePath, "w") ?: run {
            logger.errorI18n("failed_to_open_file", filePath)
            return
        }

        file.writeBom()
        fprintf(file, "%s\n", header.joinToString(separator = CSV_SEPARATOR))
        for (line in data) {
            fprintf(file, "%s\n", line)
        }
        fclose(file)
    }

    private fun CFile.writeBom() {
        val bom = cValuesOf(0xef.toByte(), 0xbb.toByte(), 0xbf.toByte())
        fwrite(bom, sizeOf<ByteVar>().toULong(), 3.toULong(), this)
    }
}
