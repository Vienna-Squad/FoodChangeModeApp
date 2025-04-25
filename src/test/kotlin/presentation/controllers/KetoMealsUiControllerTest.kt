package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.usecase.GetKetoMealUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.KetoMealsUiController
import org.example.utils.interactor.Interactor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

class KetoMealsUiControllerTest {
    private lateinit var ketoMealsUiController: KetoMealsUiController
    private val getKetoMealUseCase: GetKetoMealUseCase = mockk(relaxed = true)
    private val viewer: ItemDetailsViewer<Meal> = mockk(relaxed = true)
    private val exceptionViewer: ExceptionViewer = mockk(relaxed = true)
    private val interactor: Interactor = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        ketoMealsUiController = KetoMealsUiController(
            getKetoMealUseCase,
            viewer,
            exceptionViewer,
            interactor
        )
    }

    @Test
    fun `should show meal details when user selects like`() {
        // Given
        val meal = createKetoMeal()
        every { getKetoMealUseCase(any()) } returns meal
        every { interactor.getInput() } returns "1"

        // When
        ketoMealsUiController.execute()

        // Then
        verify { viewer.viewDetails(meal) }
    }

    @Test
    fun ` should exit when user selects invalid option`() {
        // Given
        val meal = createKetoMeal()
        every { getKetoMealUseCase(any()) } returns meal
        every { interactor.getInput() } returns "3"

        // When
        ketoMealsUiController.execute()

        // Then
        verify(exactly = 0) { viewer.viewDetails(any()) }
    }

    @Test
    fun `should show exception when no keto meals found`() {
        // Given
        val exception = NoMealFoundException("No keto meals available")
        every { getKetoMealUseCase(any()) } throws exception

        // When
        ketoMealsUiController.execute()

        // Then
        verify { exceptionViewer.viewExceptionMessage(exception) }
    }

    @Test
    fun `should show another meal when user selects dislike`() {
        // Given
        val meal1 = createKetoMeal("Meal 1")
        val meal2 = createKetoMeal("Meal 2")
        every { getKetoMealUseCase(any()) } returns meal1 andThen meal2
        every { interactor.getInput() } returns "2" andThen "1"

        // When
        ketoMealsUiController.execute()

        // Then
        verify { viewer.viewDetails(meal2) }
    }

    private fun createKetoMeal(name: String = "Keto Meal"): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = null,
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = null,
            numberOfIngredients = null
        )
    }
}