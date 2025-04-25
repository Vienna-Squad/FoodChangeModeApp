package org.example.data

import java.io.File
import kotlin.jvm.Throws

class CsvFileReader(
    private val mealsFile: File
) {

    fun getLinesAsList(): List<String> {
        val flattenContent = flattenQuotedLineBreaks(mealsFile.readText())
        return flattenContent.split("\n")
    }

    private fun flattenQuotedLineBreaks(content: String): String {

        var inQuotes = false
        val result = StringBuilder()
        var i = 0
        while (i < content.length) {
            when (content[i]) {
                '"' -> {
                    inQuotes = !inQuotes
                    result.append(content[i])
                }

                '\n', '\r' -> {
                    if (inQuotes) {
                        result.append(" ")
                    } else {
                        result.append(content[i])
                    }
                }

                else -> result.append(content[i])
            }
            i++
        }
        return result.toString().trim()
    }
}
