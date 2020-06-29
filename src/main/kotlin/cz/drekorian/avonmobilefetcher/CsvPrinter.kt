package cz.drekorian.avonmobilefetcher

import java.io.File
import java.io.OutputStream
import java.io.PrintStream

const val CSV_SEPARATOR = ";"

/**
 * This class writes Excel-compatible CSV files into a given file path.
 *
 * @author Marek Osvald
 */
object CsvPrinter {

    /**
     * Prints given [data] into a file with file path [filePath].
     *
     * @param filePath file path to store the given [data]
     * @param data data to store
     * @param header a header to write at the beginning of the CSV
     */
    fun print(filePath: String, data: Iterable<String>, vararg header: String) {

        File(filePath).outputStream().use { outputStream ->
            outputStream.writeBom()
            val stream = PrintStream(outputStream, true, Charsets.UTF_8.name())
            stream.println(header.joinToString(separator = CSV_SEPARATOR))
            data.forEach { line -> stream.println(line) }
        }
    }

    private fun OutputStream.writeBom() {
        val bom = byteArrayOf(0xef.toByte(), 0xbb.toByte(), 0xbf.toByte())
        write(bom)
    }
}
