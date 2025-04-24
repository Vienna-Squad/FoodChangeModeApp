package data

import com.google.common.truth.Truth.assertThat
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
        val line =
            "omelette,103,5,5003,2024-03-12,\"['breakfast']\",\"[250, 20, 15, 1, 1, 2, 5]\",3,\"['Crack eggs','Whisk eggs','Cook']\",\"Quick protein-rich breakfast.\",\"['eggs','salt','pepper']\",3"
        file.writeText(line)

        // When
        val expected = listOf(line)

        // Then
        assertThat(expected).containsExactlyElementsIn(csvFileReader.getLinesAsList())
    }

    @Test
    fun `getLinesAsList should return list of lines given description separated by line breaker`() {
        //Given
        val fakelines =
            "grilled cheese sandwich,101,10,5001,2024-03-10,\"['easy', 'quick']\",\"[300, 15, 25, 2, 1, 5, 10]\",4,\"['Butter bread','Add cheese','Grill','Serve']\",\"Classic cheesy snack\nperfect for quick meals.\",\"['bread','cheese','butter','salt','pepper']\",5\n" +
                    "veggie pasta,102,25,5002,2024-03-11,\"['vegetarian', 'lunch']\",\"[350, 10, 30, 3, 2, 4, 8]\",5,\"['Boil pasta','Chop veggies','Cook veggies','Mix pasta','Serve']\",\"A light and healthy pasta option\nwith fresh vegetables.\",\"['pasta','zucchini','bell pepper','onion','olive oil']\",5\n"
        //when
        file.writeText(fakelines)
        val expect = listOf(
            "grilled cheese sandwich,101,10,5001,2024-03-10,\"['easy', 'quick']\",\"[300, 15, 25, 2, 1, 5, 10]\",4,\"['Butter bread','Add cheese','Grill','Serve']\",\"Classic cheesy snack perfect for quick meals.\",\"['bread','cheese','butter','salt','pepper']\",5",
            "veggie pasta,102,25,5002,2024-03-11,\"['vegetarian', 'lunch']\",\"[350, 10, 30, 3, 2, 4, 8]\",5,\"['Boil pasta','Chop veggies','Cook veggies','Mix pasta','Serve']\",\"A light and healthy pasta option with fresh vegetables.\",\"['pasta','zucchini','bell pepper','onion','olive oil']\",5"
        )
        //then
        assertThat(expect).containsExactlyElementsIn(csvFileReader.getLinesAsList())
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
    fun `getLinesAsList should handle quoted line breaks inside fields`() {
        // Given
        val fakelines =
            "burrito,104,15,5004,2024-03-13,\"['mexican', 'dinner']\",\"[400, 12, 35, 3, 2, 6, 8]\",4,\"['Warm tortilla','Add filling','Wrap','Serve']\",\"Delicious burrito with beans\nand cheese in every bite.\",\"['tortilla','beans','cheese','salsa','lettuce']\",5\n"
        file.writeText(fakelines)

        // When
        val expected = listOf(
            "burrito,104,15,5004,2024-03-13,\"['mexican', 'dinner']\",\"[400, 12, 35, 3, 2, 6, 8]\",4,\"['Warm tortilla','Add filling','Wrap','Serve']\",\"Delicious burrito with beans and cheese in every bite.\",\"['tortilla','beans','cheese','salsa','lettuce']\",5"
        )

        // Then
        assertThat(expected).containsExactlyElementsIn(csvFileReader.getLinesAsList())
    }

    @Test
    fun `getLinesAsList should handle quoted carriage returns inside fields`() {
        // Given
        val fakelines =
            "burrito,104,15,5004,2024-03-13,\"['mexican', 'dinner']\",\"[400, 12, 35, 3, 2, 6, 8]\",4,\"['Warm tortilla','Add filling','Wrap','Serve']\",\"Delicious burrito with beans\rand cheese in every bite.\",\"['tortilla','beans','cheese','salsa','lettuce']\",5\r\n"

        file.writeText(fakelines)

        // When
        val expected = listOf(
            "burrito,104,15,5004,2024-03-13,\"['mexican', 'dinner']\",\"[400, 12, 35, 3, 2, 6, 8]\",4,\"['Warm tortilla','Add filling','Wrap','Serve']\",\"Delicious burrito with beans and cheese in every bite.\",\"['tortilla','beans','cheese','salsa','lettuce']\",5"
        )

        // Then
        assertThat(csvFileReader.getLinesAsList()).containsExactlyElementsIn(expected)
    }

}