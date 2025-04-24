package data

import com.google.common.truth.Truth.assertThat
import org.example.data.MealsCsvParser
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MealsCsvParserTest {
    private lateinit var mealsCsvParser: MealsCsvParser

    @BeforeEach
    fun setup() {
        mealsCsvParser = MealsCsvParser()
    }

    @Test
    fun `parseOneLine should return correct Meal object from CSV line`() {
        // Given
        val line =
            "Grilled Cheese Sandwich,101,10,5001,2024-03-10,\"['easy', 'quick', 'snack']\",\"[300.0, 15.0, 25.0, 2.0, 1.0, 5.0, 10.0]\",4,\"['Butter bread', 'Add cheese', 'Grill', 'Serve']\",\"Cheesy sandwich for snack\",\"['bread', 'cheese', 'butter']\",3"
        // When
        val meal = mealsCsvParser.parseOneLine(line)
        // Then
        assertThat(meal).isEqualTo(
            Meal(
                name = "Grilled Cheese Sandwich",
                id = 101L,
                minutes = 10L,
                contributorId = 5001L,
                submitted = constructDate("2024-03-10"),
                tags = listOf("easy", "quick", "snack"),
                nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
                numberOfSteps = 4,
                steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
                description = "Cheesy sandwich for snack",
                ingredients = listOf("bread", "cheese", "butter"),
                numberOfIngredients = 3
            )
        )
    }

    @Test
    fun `parseOneLine should return correct Meal object from CSV line with trailing comma last line`() {
        // Given
        val line =
            "Grilled Cheese Sandwich,101,10,5001,2024-03-10,\"['easy', 'quick', 'snack']\",\"[300.0, 15.0, 25.0, 2.0, 1.0, 5.0, 10.0]\",4,\"['Butter bread', 'Add cheese', 'Grill', 'Serve']\",\"Cheesy \"\"sandwich\"\" for snack\",\"['bread', 'cheese', 'butter']\",3,"
        // When
        val meal = mealsCsvParser.parseOneLine(line)
        // Then
        assertThat(meal).isEqualTo(
            Meal(
                name = "Grilled Cheese Sandwich",
                id = 101L,
                minutes = 10L,
                contributorId = 5001L,
                submitted = constructDate("2024-03-10"),
                tags = listOf("easy", "quick", "snack"),
                nutrition = Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
                numberOfSteps = 4,
                steps = listOf("Butter bread", "Add cheese", "Grill", "Serve"),
                description = "Cheesy \"sandwich\" for snack",
                ingredients = listOf("bread", "cheese", "butter"),
                numberOfIngredients = 3
            )
        )

    }
    @Test
    fun `parseOneLine should throw NumberFormatException when nutrition contains non-numeric values`() {
        // Given
        val badNutritionLine = "some meal,101,10,5001,2024-03-10,\"['quick']\",\"[300,10,XX,2,1,4,5]\",3,\"['Step1','Step2','Step3']\",\"desc\",\"['ing1','ing2']\",2"

        // Then
        assertThrows<NumberFormatException> {
            mealsCsvParser.parseOneLine(badNutritionLine)
        }
    }

    @Test
    fun `parseOneLine should throw exception when required fields are missing`() {
        // Given
        val incompleteLine =
            "bad meal,108,10,5002,2024-03-10,\"['quick']\",,3,\"['Do this','Do that','Serve']\",\"Some description\",\"['ingredient1','ingredient2']\",2"

        // Then
        assertThrows<Exception> {
            mealsCsvParser.parseOneLine(incompleteLine)
        }
    }

    @Test
    fun `parseOneLine should throw NumberFormatException when ID is not a number`() {
        // Given
        val badIdLine =
            "some meal,not_a_number,10,5001,2024-03-10,\"['quick']\",\"[300,10,20,2,1,4,5]\",3,\"['Step1','Step2','Step3']\",\"desc\",\"['ing1','ing2']\",2"
        // Then
        assertThrows<NumberFormatException> {
            mealsCsvParser.parseOneLine(badIdLine)
        }
    }

    @Test
    fun `parseOneLine should throw ParseException when date is invalid`() {
        // Given
        val badDateLine =
            "some meal,101,10,5001,not-a-date,\"['quick']\",\"[300,10,20,2,1,4,5]\",3,\"['Step1','Step2','Step3']\",\"desc\",\"['ing1','ing2']\",2"

        // Then
        assertThrows<ParseException> {
            mealsCsvParser.parseOneLine(badDateLine)
        }
    }

}
private fun constructDate(dateString: String): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.parse(dateString)

}
