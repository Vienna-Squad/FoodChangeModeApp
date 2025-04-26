package presentation.controllers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.usecase.GetEggFreeSweetsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.EggFreeSweetsUIController
import org.example.utils.interactor.Interactor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

class EggFreeSweetsUIControllerTest {
    private lateinit var eggFreeSweetsUIController: EggFreeSweetsUIController
    private val getEggFreeSweetsUseCase: GetEggFreeSweetsUseCase = mockk(relaxed = true)
    private val viewer: ItemDetailsViewer<Meal> = mockk(relaxed = true)
    private val exceptionViewer: ExceptionViewer = mockk(relaxed = true)
    private val interactor: Interactor = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        eggFreeSweetsUIController = EggFreeSweetsUIController(
            getEggFreeSweetsUseCase,
            viewer,
            exceptionViewer,
            interactor
        )
    }


    @Test
    fun `should show meal details when user selects like`() {
        // Given
        val meal = createMeal()
        every { getEggFreeSweetsUseCase(any()) } returns meal
        every { interactor.getInput() } returns "1"

        // When
        eggFreeSweetsUIController.execute()

        // Then
        verify { viewer.viewDetails(meal) }
    }


    @Test
    fun ` should exit when user selects invalid option`() {
        // Given
        val meal = createMeal()
        every { getEggFreeSweetsUseCase(any()) } returns meal
        every { interactor.getInput() } returns "3"

        // When
        eggFreeSweetsUIController.execute()

        // Then
        verify(exactly = 0) { viewer.viewDetails(any()) }
    }

    @Test
    fun `should show exception when no EggFreeSweets meals found`() {
        // Given
        val exception = NoMealFoundException("No EggFreeSweets meals available")
        every { getEggFreeSweetsUseCase(any()) } throws exception

        // When
        eggFreeSweetsUIController.execute()

        // Then
        verify { exceptionViewer.viewExceptionMessage(exception) }
    }


    @Test
    fun `should show another meal when user selects dislike`() {
        // Given
        val meal1 = createMeal("Meal 1")
        val meal2 = createMeal("Meal 2")
        every { getEggFreeSweetsUseCase(any()) } returns meal1 andThen meal2
        every { interactor.getInput() } returns "2" andThen "1"

        // When
        eggFreeSweetsUIController.execute()

        // Then
        verify { viewer.viewDetails(meal2) }
    }


    private fun createMeal(name: String = "Egg Free Sweet Meal"): Meal {
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