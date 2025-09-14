@file:JvmName("CsvPrinter")

package cz.drekorian.avonmobilefetcher

import java.io.File
import java.io.OutputStream
import java.io.PrintStream

actual fun printCsv(filePath: String, data: Iterable<String>, vararg header: String) {
    File(filePath).outputStream().use { outputStream ->
        outputStream.writeBom()
        val stream = PrintStream(outputStream, true, Charsets.UTF_8.name())
        stream.println(header.joinToString(separator = CSV_SEPARATOR))

        val headerSize = header.size
        data.forEach { line ->
            val lineSize = line.split(CSV_SEPARATOR).size
            require(lineSize == headerSize) {
                "CSV mismatch, current line ($line) has $lineSize columns while header has $headerSize columns."
            }

            stream.println(line)
        }
    }
}

private fun OutputStream.writeBom() {
    val bom = byteArrayOf(0xef.toByte(), 0xbb.toByte(), 0xbf.toByte())
    write(bom)
}
