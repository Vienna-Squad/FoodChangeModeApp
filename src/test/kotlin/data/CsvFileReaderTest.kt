package data

import com.google.common.truth.Truth.assertThat
import data.fakes.*
import org.example.data.CsvFileReader
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File


class CsvFileReaderTest {
    private lateinit var file: File
    private lateinit var csvFileReader: CsvFileReader

    @BeforeEach
    fun setup() {
        file = File("Test.txt")
        csvFileReader = CsvFileReader(file)
    }
    @Test
    fun `getLinesAsList should return single line correctly`() {
        // Given
        val validLine =validLine
        file.writeText(validLine)

        // When
        val expected = listOf(validLine)

        // Then
        assertThat(expected).containsExactlyElementsIn(csvFileReader.getLinesAsList())
    }

    @Test
    fun `getLinesAsList should return list of lines given description separated by line breaker`() {
        //Given
        val linesWithLineBreakerInDescrption =linesWithLineBreakerInDescrption
        //when
        file.writeText(linesWithLineBreakerInDescrption)
        val expectLinesWithLineBreakerInDescreptionAsList = linesWithLineBreakerInDescriptionAsList
        //then
        assertThat(expectLinesWithLineBreakerInDescreptionAsList).containsExactlyElementsIn(csvFileReader.getLinesAsList())
    }

    @Test
    fun `getLinesAsList should return empty list when file is empty`() {
        // Given
        file.writeText("") // empty file

        // When
        val expected = listOf("")

        // Then
        assertThat(expected).containsExactlyElementsIn(csvFileReader.getLinesAsList())
    }

    @Test
    fun `getLinesAsList should handle carriage returns inside fields`() {
        // Given
        val lineWithCarriageInDescription =lineWithCarriageInDescription

        file.writeText(lineWithCarriageInDescription)

        // When
        val expected = listOf(
            "burrito,104,15,5004,2024-03-13,\"['mexican', 'dinner']\",\"[400, 12, 35, 3, 2, 6, 8]\",4,\"['Warm tortilla','Add filling','Wrap','Serve']\",\"Delicious burrito with beans and cheese in every bite.\",\"['tortilla','beans','cheese','salsa','lettuce']\",5"
        )
        // Then
        assertThat(csvFileReader.getLinesAsList()).containsExactlyElementsIn(expected)
    }

}