package presentation.controllers

import io.mockk.*
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetMealsByProteinAndCaloriesUseCase
import org.example.logic.usecase.exceptions.NoMatchingMealsFoundException
import org.example.presentation.controllers.MealsByProteinAndCaloriesUiController
import org.example.utils.viewer.ItemsViewer
import org.junit.jupiter.api.*
import java.io.*

class MealsByProteinAndCaloriesUiControllerTest {

    private lateinit var useCase: GetMealsByProteinAndCaloriesUseCase
    private lateinit var viewer: ItemsViewer<Meal>
    private lateinit var controller: MealsByProteinAndCaloriesUiController

    private val stdOut = ByteArrayOutputStream()
    private val originalOut = System.out

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
        stdOut.reset()
    }

    private fun provideInput(vararg lines: String) {
        val input = lines.joinToString("\n")
        System.setIn(ByteArrayInputStream(input.toByteArray()))
    }

    @Test
    fun `given valid input when execute is called then show matching meals`() {
        provideInput("500", "30")
        val meals = listOf(
            createMeal(name = "Chicken Salad", calories = 500f, protein = 30f),
            createMeal(name = "Tofu Bowl", calories = 480f, protein = 32f)
        )
        every { useCase.invoke(500f, 30f) } returns meals

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Found 2 matching meals"))
        verify { viewer.viewItems(meals) }
    }

    @Test
    fun `given non-numeric calories input when execute is called then show invalid input message`() {
        provideInput("abc", "30")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Invalid input"))
        verify(exactly = 0) { useCase.invoke(any(), any()) }
        verify(exactly = 0) { viewer.viewItems(any()) }
    }

    @Test
    fun `given non-numeric protein input when execute is called then show invalid input message`() {
        provideInput("500", "xyz")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Invalid input"))
        verify(exactly = 0) { useCase.invoke(any(), any()) }
        verify(exactly = 0) { viewer.viewItems(any()) }
    }

    @Test
    fun `given valid input and use case throws IllegalArgumentException when execute is called then show error message`() {
        provideInput("500", "30")
        every { useCase.invoke(500f, 30f) } throws IllegalArgumentException("Bad input")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Error: Bad input"))
        verify(exactly = 0) { viewer.viewItems(any()) }
    }

    @Test
    fun `given valid input and use case throws NoMatchingMealsFoundException when execute is called then show warning message`() {
        provideInput("500", "30")
        every { useCase.invoke(500f, 30f) } throws NoMatchingMealsFoundException("No meals found")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("No meals found"))
        verify(exactly = 0) { viewer.viewItems(any()) }
    }

    @Test
    fun `given valid input and use case throws generic exception when execute is called then show unexpected error message`() {
        provideInput("500", "30")
        every { useCase.invoke(500f, 30f) } throws RuntimeException("Something went wrong")

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Unexpected error: Something went wrong"))
        verify(exactly = 0) { viewer.viewItems(any()) }
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