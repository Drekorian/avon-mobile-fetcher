package cz.drekorian.avonmobilefetcher.flow.linenumber

class LineNumberFlow {

    private val reader: LineNumberReader = LineNumberReader()

    fun fetchLineNumbers(): List<String> {
        val read = reader.readLineNumbers()
        println("Read line numbers ${read.size}")

        return buildList {
            for (code in (0..9999)) {
                if (code in read.keys) {
                    add("$code${read[code]}".padStart(5, '0'))
                } else {
                    for (controlNumber in (0..9)) {
                        add("$code$controlNumber".padStart(5, '0'))
                    }
                }
            }
        }
    }
}
