package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetMealsOfCountryUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException

import org.example.presentation.controllers.MealByCountryUiController
import org.example.utils.interactor.Interactor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class MealByCountryUiControllerTest {
    private lateinit var getMealsOfCountryUseCase: GetMealsOfCountryUseCase
    private lateinit  var viewer: ItemsViewer<Meal>
    private lateinit  var exceptionViewer: ExceptionViewer
    private lateinit var mealByCountryUiController: MealByCountryUiController
    private lateinit var interactor: Interactor

    @BeforeEach
    fun setup() {
        getMealsOfCountryUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        exceptionViewer=mockk(relaxed = true)
        interactor= mockk(relaxed = true)
        mealByCountryUiController = MealByCountryUiController(getMealsOfCountryUseCase, viewer,exceptionViewer,interactor,)
    }

    @Test
    fun `should show meals when country has matching meals`() {
        // Given
        val meal = createMeal(
            name = "Koshari",
            tags = listOf("egyptian"),
            description = "A traditional Egyptian street food"
        )
        every { interactor.getInput() } returns "egypt"
        every { getMealsOfCountryUseCase("egypt") } returns listOf(meal)

        // When
        mealByCountryUiController.execute()

        // Then
        verify { viewer.viewItems(listOf(meal)) }
    }

    @Test
    fun `should show exception message when getMealsOfCountryUseCase throws NoMealFoundException for no matching meals`() {
        // Given
        val exception = NoMealFoundException("No meals found for country 'france'.")
        every { interactor.getInput() } returns "epgtanoo"
        every { getMealsOfCountryUseCase("epgtanoo") } throws exception

        // When
        mealByCountryUiController.execute()

        // Then
        verify { exceptionViewer.viewExceptionMessage(exception) }

    }

    @Test
    fun `should show exception message when getMealsOfCountryUseCase throws NoMealFoundException for empty input`() {
        // Given
        val exception = NoMealFoundException("Country name cannot be empty.")
        every { interactor.getInput() } returns "   "
        every { getMealsOfCountryUseCase("   ") } throws exception

        // When
        mealByCountryUiController.execute()

        // Then
        verify { exceptionViewer.viewExceptionMessage(exception) }
    }


    private fun createMeal(
        name: String,
        id: Long = 1L,
        tags: List<String>? = null,
        description: String? = null,
        minutes: Long = 45L,
        contributorId: Long = 101L,
        nutrition: Nutrition = Nutrition(
            calories = 700f,
            totalFat = 15f,
            sugar = 5f,
            sodium = 300f,
            protein = 20f,
            saturatedFat = 3f,
            carbohydrates = 120f
        ),
        numberOfSteps: Int = 5,
        steps: List<String> = listOf("Step 1", "Step 2"),
        ingredients: List<String> = listOf("ingredient1", "ingredient2"),
        numberOfIngredients: Int = 2
    ): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = minutes,
            contributorId = contributorId,
            submitted = Date(),
            tags = tags,
            nutrition = nutrition,
            numberOfSteps = numberOfSteps,
            steps = steps,
            description = description,
            ingredients = ingredients,
            numberOfIngredients = numberOfIngredients
        )
    }
}