package cz.drekorian.avonmobilefetcher

internal const val CSV_SEPARATOR = ";"

/**
 * This class writes Excel-compatible CSV files into a given file path.
 *
 * @author Marek Osvald
 */

expect object CsvPrinter {

    /**
     * Prints given [data] into a file with file path [filePath].
     *
     * @param filePath file path to store the given [data]
     * @param data data to store
     * @param header a header to write at the beginning of the CSV
     */
    fun print(filePath: String, data: Iterable<String>, vararg header: String)
}
