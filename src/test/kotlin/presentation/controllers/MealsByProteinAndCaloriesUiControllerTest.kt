package presentation.controllers

import io.mockk.*
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetMealsByProteinAndCaloriesUseCase
import org.example.logic.usecase.exceptions.NoMatchingMealsFoundException
import org.example.presentation.Viewer
import org.example.presentation.controllers.MealsByProteinAndCaloriesUiController
import org.junit.jupiter.api.*
import java.io.*

class MealsByProteinAndCaloriesUiControllerTest {

    private lateinit var useCase: GetMealsByProteinAndCaloriesUseCase
    private lateinit var viewer: Viewer
    private lateinit var controller: MealsByProteinAndCaloriesUiController

    private val stdOut = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalIn = System.`in`

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk()
        controller = MealsByProteinAndCaloriesUiController(useCase, viewer)
        System.setOut(PrintStream(stdOut))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        System.setIn(originalIn)
        stdOut.reset()
    }

    private fun provideInput(vararg lines: String) {
        val input = lines.joinToString("\n")
        System.setIn(ByteArrayInputStream(input.toByteArray()))
    }

    @Test
    fun `execute should show meals when valid input is provided`() {
        provideInput("500", "30")
        val meals = listOf(
            createMeal(name = "Chicken Salad", calories = 500f, protein = 30f),
            createMeal(name = "Tofu Bowl", calories = 480f, protein = 32f)
        )
        every { useCase.invoke(500f, 30f) } returns meals

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Found 2 matching meals"))
        verify { viewer.showMealsDetails(meals) }
    }

    @Test
    fun `execute should print invalid input message if calories is not a number`() {
        provideInput("abc", "30")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Invalid input"))
        verify(exactly = 0) { useCase.invoke(any(), any()) }
        verify(exactly = 0) { viewer.showMealsDetails(any()) }
    }

    @Test
    fun `execute should print invalid input message if protein is not a number`() {
        provideInput("500", "xyz")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Invalid input"))
        verify(exactly = 0) { useCase.invoke(any(), any()) }
        verify(exactly = 0) { viewer.showMealsDetails(any()) }
    }

    @Test
    fun `execute should print error message for IllegalArgumentException`() {
        provideInput("500", "30")
        every { useCase.invoke(500f, 30f) } throws IllegalArgumentException("Bad input")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Error: Bad input"))
        verify(exactly = 0) { viewer.showMealsDetails(any()) }
    }

    @Test
    fun `execute should print warning for NoMatchingMealsFoundException`() {
        provideInput("500", "30")
        every { useCase.invoke(500f, 30f) } throws NoMatchingMealsFoundException("No meals found")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("No meals found"))
        verify(exactly = 0) { viewer.showMealsDetails(any()) }
    }

    @Test
    fun `execute should print unexpected error for generic exception`() {
        provideInput("500", "30")
        every { useCase.invoke(500f, 30f) } throws RuntimeException("Something went wrong")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Unexpected error: Something went wrong"))
        verify(exactly = 0) { viewer.showMealsDetails(any()) }
    }

    private fun createMeal(name: String, calories: Float?, protein: Float?): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = java.util.Date(),
            tags = null,
            nutrition = if (calories != null || protein != null) {
                Nutrition(
                    calories = calories,
                    totalFat = null,
                    sugar = null,
                    sodium = null,
                    protein = protein,
                    saturatedFat = null,
                    carbohydrates = null
                )
            } else {
                null
            },
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = null,
            numberOfIngredients = null
        )
    }
}