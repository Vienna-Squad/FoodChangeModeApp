package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.usecase.GetIraqiMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.IraqiMealsUIController
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class IraqiMealsUIControllerTest {
    private lateinit var iraqiMealsUIController: IraqiMealsUIController
    private val viewer: ItemsViewer<Meal> = mockk(relaxed = true)
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase = mockk(relaxed = true)
    private val exceptionViewer: ExceptionViewer = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        iraqiMealsUIController = IraqiMealsUIController(getIraqiMealsUseCase, viewer, exceptionViewer)
    }

    @Test
    fun `should show iraqi meals when use case returns them`() {
        //given
        val meals = listOf(
            createIraqiMeal(),
            createIraqiMeal(),
            createIraqiMeal(),
        )
        every { getIraqiMealsUseCase() } returns meals
        //when
        iraqiMealsUIController.execute()
        //then
        verify { viewer.viewItems(meals) }
    }

    @Test
    fun `should show exception when use case throws NoMealFoundException`() {
        //given
        val exception = NoMealFoundException("iraqi meals")
        every { getIraqiMealsUseCase() } throws exception
        //when
        iraqiMealsUIController.execute()
        //then
        verify { exceptionViewer.viewExceptionMessage(exception) }
    }

    private fun createIraqiMeal() =
        Meal(
            name = null,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = listOf("iraqi", "spicy"),
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = "Popular meal in Iraq",
            ingredients = null,
            numberOfIngredients = null
        )
}