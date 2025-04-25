package data

import com.google.common.truth.Truth.assertThat
import data.fakes.lineWithEmptyDescription
import data.fakes.parsedMeal
import data.fakes.parsedMealWithNullDescription
import data.fakes.validListOfRows
import io.mockk.every
import io.mockk.mockk
import org.example.data.CsvFileReader
import org.example.data.CsvMealsRepository
import org.example.data.MealsCsvParser
import org.example.logic.repository.MealsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CsvMealsRepositoryTest {
    private lateinit var mealsCsvParser: MealsCsvParser
    private lateinit var csvFileReader: CsvFileReader
    private lateinit var mealRepository: MealsRepository

    @BeforeEach
    fun setup() {
        csvFileReader = mockk(relaxed = true)
        mealsCsvParser = mockk(relaxed = true)
        mealRepository = CsvMealsRepository(csvFileReader, mealsCsvParser)
    }

    @Test
    fun `getAllMeals should return meals parsed from real CSV`() {
        // Given
        every { csvFileReader.getLinesAsList() }returns validListOfRows
        val parsedMeal = parsedMeal
        every { mealsCsvParser.parseLineIntoMeal(any()) } returns parsedMeal
        // When
        val actualMeals = mealRepository.getAllMeals()
        // Then

        assertThat(actualMeals).containsExactlyElementsIn(listOf(parsedMeal))
    }


    @Test
    fun `getAllMeals should return meal with null description when description is empty`() {
        // Given
        val lineWithEmptyDescription =lineWithEmptyDescription

        every { csvFileReader.getLinesAsList() } returns listOf("emptyline",lineWithEmptyDescription)

        val parsedMealWithNullDescription = parsedMealWithNullDescription

        every { mealsCsvParser.parseLineIntoMeal(any()) } returns parsedMealWithNullDescription

        // When
        val actualMeals = mealRepository.getAllMeals()

        // Then
        assertThat(actualMeals).containsExactlyElementsIn(listOf(parsedMealWithNullDescription))
    }

    @Test
    fun `getAllMeals should return empty list when CSV has no data`() {
        // Given
        every { csvFileReader.getLinesAsList() } returns emptyList()

        // When
        val actualMeals = mealRepository.getAllMeals()

        // Then
        assertThat(actualMeals).isEmpty()
    }
}


