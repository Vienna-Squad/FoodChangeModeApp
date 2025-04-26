package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.usecase.GetHighCalorieMealUseCase
import org.example.presentation.controllers.HighCalorieMealUIController
import org.example.utils.interactor.HighCalorieMealInteractor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HighCalorieMealUIControllerTest {
    private val suggestHighCalorieMealUseCase: GetHighCalorieMealUseCase = mockk(relaxed = true)
    private val showMealDetailsViewer: ItemDetailsViewer<Meal> = mockk(relaxUnitFun = true)
    private val highCalorieMealViewer: ItemDetailsViewer<Meal> = mockk(relaxUnitFun = true)
    private val anotherSuggestionMealViewer: ItemDetailsViewer<String> = mockk(relaxUnitFun = true)
    private val exceptionViewer: ExceptionViewer = mockk(relaxUnitFun = true)
    private val highCalorieMealInteractor: HighCalorieMealInteractor = mockk()

    lateinit var highCalorieMealUIController: HighCalorieMealUIController

    @BeforeEach
    fun setUp() {
        highCalorieMealUIController = HighCalorieMealUIController(
            suggestHighCalorieMealUseCase = suggestHighCalorieMealUseCase,
            showMealDetailsViewer = showMealDetailsViewer,
            highCalorieMealViewer = highCalorieMealViewer,
            anotherSuggestionMealViewer = anotherSuggestionMealViewer,
            exceptionViewer = exceptionViewer,
            highCalorieMealInteractor = highCalorieMealInteractor
        )
    }


    @Test
    fun `execute should call getRandomHighCalorieMeal once i call execute to get random high calorie meal`() {

        highCalorieMealUIController.execute()

        verify { suggestHighCalorieMealUseCase.getRandomHighCalorieMeal() }

    }

    @Test
    fun `execute should call getInput of highCalorieMealInteractor whan handling user input`() {

        highCalorieMealUIController.execute()

        verify { highCalorieMealInteractor.getInput()}

    }

    @Test
    fun `execute should call getRandomHighCalorieMeal viewDetails of highCalorieMealUIController`() {

        highCalorieMealUIController.execute()

        verify { highCalorieMealViewer.viewDetails(any()) }
    }

    @Test
    fun `execute should call viewDetails of showMealDetailsViewer when input number is 1`() {

        every { highCalorieMealInteractor.getInput() } returns 1

        highCalorieMealUIController.execute()

        verify { showMealDetailsViewer.viewDetails(any()) }

    }


    @Test
    fun `execute should call getRandomHighCalorieMeal viewDetails of anotherSuggestionMealViewer when input number is 2`() {

        every { highCalorieMealInteractor.getInput() } returns 2

        highCalorieMealUIController.execute()

        verify { anotherSuggestionMealViewer.viewDetails(any()) }

    }



    @Test
    fun `execute should throw  InvalidInputNumberOfHighCalorieMeal when the input number is not in range from 1 to 3`() {

        every { highCalorieMealInteractor.getInput() } returns 4

        highCalorieMealUIController.execute()

        verify { exceptionViewer.viewExceptionMessage(any()) }

    }


}