package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.usecase.GetIraqiMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.Viewer
import org.example.presentation.controllers.IraqiMealsUIController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class IraqiMealsUIControllerTest {
    private lateinit var iraqiMealsUIController: IraqiMealsUIController
    private val viewer: Viewer = mockk(relaxed = true)
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        iraqiMealsUIController = IraqiMealsUIController(getIraqiMealsUseCase, viewer)
    }

    @Test
    fun `when call execute() in iraqiMealsUIController and getIraqiMealsUseCase returns list of meals should viewer show the meals details`() {
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
        verify { viewer.showMealsDetails(meals) }
    }

    @Test
    fun `when call execute() in iraqiMealsUIController and getIraqiMealsUseCase throws NoMealFoundException should viewer show the exception message`() {
        //given
        val exception = NoMealFoundException("iraqi meals")
        every { getIraqiMealsUseCase() } throws exception
        //when
        iraqiMealsUIController.execute()
        //then
        verify { viewer.showExceptionMessage(exception) }
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