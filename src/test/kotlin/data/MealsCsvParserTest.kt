package data

import com.google.common.truth.Truth.assertThat
import data.fakes.*
import org.example.data.MealsCsvParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.ParseException

class MealsCsvParserTest {
    private lateinit var mealsCsvParser: MealsCsvParser

    @BeforeEach
    fun setup() {
        mealsCsvParser = MealsCsvParser()
    }

    @Test
    fun `parseLineIntoMeal should return correct Meal object from CSV line`() {
        // Given
        val validLine = validLine
        // When
        val meal = mealsCsvParser.parseLineIntoMeal(validLine)
        // Then
        assertThat(meal).isEqualTo(
            validMeal
        )
    }

    @Test
    fun `parseLineIntoMeal should return correct Meal object from CSV line with trailing comma last line`() {
        // Given
        val validLineTrailingComma = validLineTrailingComma
        // When
        val validMeal = mealsCsvParser.parseLineIntoMeal(validLineTrailingComma)
        // Then
        assertThat(validMeal).isEqualTo(
            validMeal
        )

    }

    @Test
    fun `parseLineIntoMeal should return correct Meal object with word in description surrounded by double quotation`() {
        // Given
        val validLineWordInDoubleQuotation = validLineWordInDoubleQuotation
        // When
        val validMeal = mealsCsvParser.parseLineIntoMeal(validLineWordInDoubleQuotation)
        // Then
        assertThat(validMeal).isEqualTo(
            validMealWordInDoubleQuotation
        )
    }


    @Test
    fun `parseLineIntoMeal should throw NumberFormatException when nutrition contains non-numeric values`() {
        // Given
        val invalidNutritionValue = invalidNutritionValue
        // Then
        assertThrows<NumberFormatException> {
            mealsCsvParser.parseLineIntoMeal(invalidNutritionValue)
        }
    }

    @Test
    fun `parseLineIntoMeal should throw exception when required fields are missing`() {
        // Given
        val missingFieldLine = missingFieldLine

        // Then
        assertThrows<Exception> {
            mealsCsvParser.parseLineIntoMeal(missingFieldLine)
        }
    }

    @Test
    fun `parseLineIntoMeal should throw NumberFormatException when ID is not a number`() {
        // Given
        val invalidIdNumber = invalidIdNumber
        // Then
        assertThrows<NumberFormatException> {
            mealsCsvParser.parseLineIntoMeal(invalidIdNumber)
        }
    }

    @Test
    fun `parseOneLine should throw ParseException when date is invalid`() {
        // Given
        val invalidDateFormat = invalidDateFormat
        // Then
        assertThrows<ParseException> {
            mealsCsvParser.parseLineIntoMeal(invalidDateFormat)
        }
    }

}
