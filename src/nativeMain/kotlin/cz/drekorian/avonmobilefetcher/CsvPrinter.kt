package cz.drekorian.avonmobilefetcher

import cz.drekorian.avonmobilefetcher.multiplatform.CFile
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.sizeOf
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fprintf
import platform.posix.fwrite

/**
 * This class writes Excel-compatible CSV files into a given file path.
 *
 * @author Marek Osvald
 */
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
        fwrite(bom, sizeOf<ByteVar>().toULong(), 3, this)
    }
}
