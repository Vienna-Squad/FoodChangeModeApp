package presentation.controllers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.usecase.GetRandomPotatoMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.PotatoesMealsUiController
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.junit.jupiter.api.BeforeEach
import java.util.Date
import kotlin.test.Test

class PotatoesMealsUiControllerTest {
    private lateinit var potatoesMealsUiController: PotatoesMealsUiController
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase = mockk(relaxed = true)
    private val viewer: ItemsViewer<Meal> = mockk(relaxed = true)
    private val exceptionViewer: ExceptionViewer = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        potatoesMealsUiController = PotatoesMealsUiController(getRandomPotatoMealsUseCase, viewer, exceptionViewer)
    }

    @Test
    fun `should returns list of meals has potatoes when call execute() in potatoesMealsUiController`() {
        //given
        val meals = listOf(
            createPotatoMeal(),
            createPotatoMeal(),
            createPotatoMeal(),
        )
        every { getRandomPotatoMealsUseCase() } returns meals
        //when
        potatoesMealsUiController.execute()
        //then
        verify { viewer.viewItems(meals) }
    }
    @Test
    fun `should show exception message when call execute() in potatoesMealsUiController`() {
        //given
        val exception = NoMealFoundException("Not found Potatoes Meals")
        every { getRandomPotatoMealsUseCase() } throws exception
        //when
        potatoesMealsUiController.execute()
        //then
        verify { exceptionViewer.viewExceptionMessage(exception) }
    }


    private fun createPotatoMeal() =
        Meal(
            name = null,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = null,
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = listOf("Potatoes"),
            numberOfIngredients = null
        )

}