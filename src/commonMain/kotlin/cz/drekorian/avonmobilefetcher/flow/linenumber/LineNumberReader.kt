package cz.drekorian.avonmobilefetcher.flow.linenumber

expect class LineNumberReader() {

    fun readLineNumbers(filePath: String = "LineNumber.txt"): Map<Int, Int>
}
