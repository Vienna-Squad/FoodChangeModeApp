package presentation.controllers

import io.mockk.*
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.logic.usecase.exceptions.NoHealthyFastFoodFoundException
import org.example.presentation.Viewer
import org.example.presentation.controllers.HealthyFastFoodUIController
import org.junit.jupiter.api.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import com.google.common.truth.Truth.assertThat

class HealthyFastFoodUIControllerTest {

    private lateinit var useCase: GetHealthyFastFoodUseCase
    private lateinit var viewer: Viewer
    private lateinit var controller: HealthyFastFoodUIController

    private val stdOut = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        controller = HealthyFastFoodUIController(useCase, viewer)
        System.setOut(PrintStream(stdOut))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        stdOut.reset()
    }

    @Test
    fun `execute should show meals when use case returns data`() {
        val meals = listOf(
            createHealthyFastFoodMeal("High Fat Meal", 20, 20f, 6f, 40f),
            createHealthyFastFoodMeal("Slow Meal", 30, 5f, 1f, 10f)
        )
        every { useCase.invoke() } returns meals

        controller.execute()

        val output = stdOut.toString()
        assertThat(output).contains("Healthy Fast Food Suggestions")
        verify { viewer.showMealsDetails(meals) }
    }

    @Test
    fun `execute should print warning when NoHealthyFastFoodFoundException is thrown`() {
        val exception = NoHealthyFastFoodFoundException("No healthy fast food found")
        every { useCase.invoke() } throws exception

        controller.execute()

        val output = stdOut.toString()
        assertThat(output).contains("No healthy fast food found")
        verify(exactly = 0) { viewer.showMealsDetails(any()) }
    }

    @Test
    fun `execute should print error when generic exception is thrown`() {
        val exception = RuntimeException("Something went wrong")
        every { useCase.invoke() } throws exception

        controller.execute()

        val output = stdOut.toString()
        assertThat(output).contains("Error fetching healthy fast food: Something went wrong")
        verify(exactly = 0) { viewer.showMealsDetails(any()) }
    }

    private fun createHealthyFastFoodMeal(
        name: String = "Test Meal",
        minutes: Long = 15,
        totalFat: Float = 20f,
        saturatedFat: Float = 5f,
        carbs: Float = 25f
    ): Meal {
        return Meal(
            name = name,
            nutrition = Nutrition(
                calories = 500f,
                totalFat = totalFat,
                protein = 30f,
                sugar = 5f,
                sodium = 200f,
                saturatedFat = saturatedFat,
                carbohydrates = carbs
            ),
            id = 1,
            minutes = minutes,
            contributorId = 1,
            submitted = java.util.Date(),
            tags = listOf("test"),
            numberOfSteps = 1,
            steps = listOf("Step 1"),
            description = "Test meal",
            ingredients = listOf("Ingredient 1"),
            numberOfIngredients = 1
        )
    }
}