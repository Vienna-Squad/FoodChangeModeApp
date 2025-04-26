package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.ItalianMealForGroupsUiController
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ItalianMealForGroupsUiControllerTest {

    private lateinit var getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase
    private lateinit var viewer: ItemsViewer<Meal>
    private lateinit var exceptionViewer: ExceptionViewer
    private lateinit var italianMealForGroupsUiController: ItalianMealForGroupsUiController

    @BeforeEach
    fun setup() {
        getItalianGroupMealsUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        exceptionViewer=mockk(relaxed = true)
        italianMealForGroupsUiController = ItalianMealForGroupsUiController(getItalianGroupMealsUseCase, viewer,exceptionViewer)
    }

    @Test
    fun `should display meals when Italian group meals are found`() {
        // Given
        val meal = Meal(
            name = "Pizza",
            id = 1L,
            minutes = 45L,
            contributorId = 101L,
            submitted = Date(),
            tags = listOf("italian", "for-large-groups"),
            nutrition = Nutrition(
                calories = 700f,
                totalFat = 15f,
                sugar = 5f,
                sodium = 300f,
                protein = 20f,
                saturatedFat = 3f,
                carbohydrates = 120f
            ),
            numberOfSteps = 5,
            steps = listOf("Step 1", "Step 2"),
            description = "A classic Italian dish",
            ingredients = listOf("dough", "cheese"),
            numberOfIngredients = 2
        )
        val meals = listOf(meal)
        every { getItalianGroupMealsUseCase() } returns meals

        // When
        italianMealForGroupsUiController.execute()

        // Then
        verify { viewer.viewItems(meals) }

    }

    @Test
    fun `should show exception message when no Italian group meals are found`() {
        // Given
        val exception = NoMealFoundException("No meals found with tags 'italian' and 'for-large-groups' in the Csv file.")
        every { getItalianGroupMealsUseCase() } throws exception

        // When
        italianMealForGroupsUiController.execute()

        // Then
        verify { exceptionViewer.viewExceptionMessage(exception) }
    }

}