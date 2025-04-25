package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.usecase.GuessPrepareTimeGameUseCase
import org.example.logic.usecase.exceptions.GameOverException
import org.example.logic.usecase.exceptions.InvalidMinutesException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.logic.usecase.exceptions.TooHighException
import org.example.logic.usecase.exceptions.TooLowException
import org.example.presentation.controllers.PreparationTimeGuessingGameUiController
import org.example.utils.interactor.Interactor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class PreparationTimeGuessingGameUiControllerTest {
    private lateinit var preparationTimeGuessingGameUiController: PreparationTimeGuessingGameUiController
    private val guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase = mockk(relaxed = true)
    private val exceptionViewer: ExceptionViewer = mockk(relaxed = true)
    private val interactor: Interactor = mockk(relaxed = true)
    private val messageViewer: ItemDetailsViewer<String> = mockk(relaxed = true)
    private val meal = createMeal("pizza", 45)

    @BeforeEach
    fun setup() {
        preparationTimeGuessingGameUiController = PreparationTimeGuessingGameUiController(
            guessPrepareTimeGameUseCase = guessPrepareTimeGameUseCase,
            exceptionViewer = exceptionViewer,
            interactor = interactor,
            messageViewer = messageViewer
        )
    }

    @Test
    fun `when call getRandomMeal throws NoMealFoundException should viewExceptionMessage be called`() {
        //given
        val exception = NoMealFoundException()
        every { guessPrepareTimeGameUseCase.getRandomMeal() } throws exception
        //when
        preparationTimeGuessingGameUiController.execute()
        //then
        verify {
            exceptionViewer.viewExceptionMessage(exception)
        }
    }

    @Test
    fun `when call getRandomMeal but its minutes attribute is null should viewExceptionMessage be called with InvalidMinutesException`() {
        //given
        val randomMeal = createMeal()
        every { guessPrepareTimeGameUseCase.getRandomMeal() } returns randomMeal
        //when
        preparationTimeGuessingGameUiController.execute()
        //then
        verify { exceptionViewer.viewExceptionMessage(match { it is InvalidMinutesException }) }
    }

    @Test
    fun `when guess function throw TooHighException should let the user try again`() {
        //given
        val exception = TooHighException(2)
        every { interactor.getInput() } returns "60"
        every { guessPrepareTimeGameUseCase.guess(any(), any(), 3) } throws exception
        //when
        preparationTimeGuessingGameUiController.execute()
        //then
        verify { messageViewer.viewDetails(match { it == "${exception.message}, try again: " }) }
        verify { interactor.getInput() }
    }

    @Test
    fun `when guess function throw TooLowException should let the user try again`() {
        //given
        val exception = TooLowException(2)
        every { interactor.getInput() } returns "20"
        every { guessPrepareTimeGameUseCase.guess(any(), any(), 3) } throws exception
        //when
        preparationTimeGuessingGameUiController.execute()
        //then
        verify { messageViewer.viewDetails(match { it == "${exception.message}, try again: " }) }
        verify { interactor.getInput() }
    }

    @Test
    fun `when guess function throw GameOverException should viewExceptionMessage be called`() {
        //given
        val exception = GameOverException(meal.minutes!!)
        every { interactor.getInput() } returns "60"
        every { guessPrepareTimeGameUseCase.guess(any(), any(), 3) } throws exception
        //when
        preparationTimeGuessingGameUiController.execute()
        //then
        verify { exceptionViewer.viewExceptionMessage(exception) }
    }

    @Test
    fun `when user guessed correct answer on first try should break loop and show congratulation message`() {
        //given
        val message = GuessPrepareTimeGameUseCase.CONGRATULATION_MESSAGE
        every { interactor.getInput() } returns meal.minutes.toString()
        every { guessPrepareTimeGameUseCase.guess(any(), any(), 3) } returns message
        //when
        preparationTimeGuessingGameUiController.execute()
        //then
        verify { messageViewer.viewDetails(message) }
        verify(exactly = 1) { interactor.getInput() }
    }

    @Test
    fun `when user enter character should viewExceptionMessage be called`() {
        // given
        every { interactor.getInput() } returns "a"
        // when
        preparationTimeGuessingGameUiController.execute()
        // then
        verify { exceptionViewer.viewExceptionMessage(match { it is InvalidMinutesException }) }
    }


    private fun createMeal(name: String? = null, minutes: Long? = null) =
        Meal(
            name = name,
            id = null,
            minutes = minutes,
            contributorId = null,
            submitted = Date(),
            tags = listOf("egyptian", "spicy"),
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = "Popular meal in Egypt",
            ingredients = null,
            numberOfIngredients = null
        )
}