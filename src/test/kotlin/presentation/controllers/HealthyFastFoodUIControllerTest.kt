package presentation.controllers

import io.mockk.*
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.logic.usecase.exceptions.NoHealthyFastFoodFoundException
import org.example.presentation.controllers.HealthyFastFoodUIController
import org.junit.jupiter.api.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import com.google.common.truth.Truth.assertThat
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemsViewer

class HealthyFastFoodUIControllerTest {

    private lateinit var controller: HealthyFastFoodUIController
    private val useCase: GetHealthyFastFoodUseCase = mockk(relaxed = true)
    private val viewer: ItemsViewer<Meal> = mockk(relaxed = true)
    private val viewerException: ExceptionViewer = mockk(relaxed = true)

    private val stdOut = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        controller = HealthyFastFoodUIController(useCase, viewer, viewerException)
        System.setOut(PrintStream(stdOut))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        stdOut.reset()
    }

    @Test
    fun `given healthy meals when executing then meals should be shown`() {
        // Given
        val meals = listOf(
            createHealthyFastFoodMeal("High Fat Meal", 20, 20f, 6f, 40f),
            createHealthyFastFoodMeal("Slow Meal", 30, 5f, 1f, 10f)
        )
        every { useCase.invoke() } returns meals

        // When
        controller.execute()

        // Then
        val output = stdOut.toString()
        assertThat(output).contains("Healthy Fast Food Suggestions")
        verify { viewer.viewItems(meals) }
    }

    @Test
    fun `given no healthy meals when executing then warning should be printed`() {
        // Given
        val exception = NoHealthyFastFoodFoundException("No healthy fast food found")
        every { useCase.invoke() } throws exception

        // When
        controller.execute()

        // Then
        verify { viewerException.viewExceptionMessage(match { it.message == "No healthy fast food found" }) }
    }

    @Test
    fun `given unexpected error when executing then generic error should be printed`() {
        // Given
        val exception = RuntimeException("Something went wrong")
        every { useCase() } throws exception
        // When
        controller.execute()
        // Then
        verify { viewerException.viewExceptionMessage(match { it.message == "Something went wrong" }) }
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