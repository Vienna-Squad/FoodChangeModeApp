package data

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.data.CsvFileReader
import org.example.data.CsvMealsRepository
import org.example.data.MealsCsvParser
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

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
        val validCsvLine = "emptyline\nGrilled Cheese Sandwich,101,10,5001,2024-03-10," +
                "\"['easy','quick']\",\"[300.0,15.0,25.0,2.0,1.0,5.0,10.0]\",4," +
                "\"['Butter bread','Add cheese','Grill','Serve']\",\"A cheesy snack\"," +
                "\"['bread','cheese','butter']\",3"

        every { csvFileReader.getLinesAsList() } returns listOf(
            "emptyline", "Grilled Cheese Sandwich,101,10,5001,2024-03-10,\" +\n" +
                    " \"\\\"['easy','quick']\\\",\\\"[300.0,15.0,25.0,2.0,1.0,5.0,10.0]\\\",4,\" +\n" +
                    " \"\\\"['Butter bread','Add cheese','Grill','Serve']\\\",\\\"A cheesy snack\\\",\" +\n" +
                    "\"\\\"['bread','cheese','butter']\\\",3\""
        )

        val parsedMeal = Meal(
            name = "Grilled Cheese Sandwich",
            id = 101,
            minutes = 10,
            contributorId = 5001,
            submitted = constructDate("2024-03-10"),
            tags = listOf("easy", "quick"),
            nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
            numberOfSteps = 4,
            steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
            description = "A cheesy snack",
            ingredients = listOf("bread", "cheese", "butter"),
            numberOfIngredients = 3
        )
        every { mealsCsvParser.parseOneLine(any()) } returns parsedMeal
        // When
        val actualMeals = mealRepository.getAllMeals()
        // Then

        assertThat(actualMeals).containsExactlyElementsIn(listOf(parsedMeal))
    }


    @Test
    fun `getAllMeals should return meal with null description when description is empty`() {
        // Given
        val lineWithEmptyDescription =
            "Grilled Cheese Sandwich,101,10,5001,2024-03-10," +
                    "\"['easy','quick']\",\"[300.0,15.0,25.0,2.0,1.0,5.0,10.0]\",4," +
                    "\"['Butter bread','Add cheese','Grill','Serve']\",\"\",\"['bread','cheese','butter']\",3"

        every { csvFileReader.getLinesAsList() } returns listOf(
            "emptyline",lineWithEmptyDescription)

        val parsedMealWithNullDescription = Meal(
            name = "Grilled Cheese Sandwich",
            id = 101,
            minutes = 10,
            contributorId = 5001,
            submitted = constructDate("2024-03-10"),
            tags = listOf("easy", "quick"),
            nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
            numberOfSteps = 4,
            steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
            description = null, // <- Important part
            ingredients = listOf("bread", "cheese", "butter"),
            numberOfIngredients = 3
        )

        every { mealsCsvParser.parseOneLine(any()) } returns parsedMealWithNullDescription

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


private fun constructDate(dateString: String): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.parse(dateString)

}