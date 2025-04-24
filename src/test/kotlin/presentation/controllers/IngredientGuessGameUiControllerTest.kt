package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.usecase.GuessIngredientGameUseCase
import org.example.logic.usecase.IngredientGameDetails
import org.example.logic.usecase.exceptions.IngredientUserInputException
import org.example.presentation.controllers.IngredientGuessGameUiController
import org.example.utils.interactor.InteractorNumber
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.IngredientGameDetailsViewer
import org.example.utils.viewer.utils.viewer.IngredientGameScoreViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IngredientGuessGameUiControllerTest {

    private val guessIngredientGameUseCase: GuessIngredientGameUseCase = mockk(relaxed = true)
    private val ingredientDetailsViewer: IngredientGameDetailsViewer = mockk(relaxUnitFun = true)
    private val scoreViewer: IngredientGameScoreViewer = mockk(relaxUnitFun = true)
    private val exceptionViewer: ExceptionViewer = mockk(relaxUnitFun = true)
    private val interactorNumber: InteractorNumber = mockk(relaxed = true)

    private lateinit var ingredientGuessGameUiController: IngredientGuessGameUiController


    @BeforeEach
    fun setUp() {
        ingredientGuessGameUiController = IngredientGuessGameUiController(
            guessIngredientGameUseCase = guessIngredientGameUseCase,
            ingredientDetailsViewer = ingredientDetailsViewer,
            scoreViewer = scoreViewer,
            exceptionViewer = exceptionViewer,
            interactorNumber = interactorNumber
        )
    }

    @Test
    fun `execute should call get game details of guessIngredientGameUseCase`() {
        // when
        ingredientGuessGameUiController.execute()

        // then
        verify { ingredientDetailsViewer.viewDetails(any()) }
    }

    @Test
    fun `execute should call viewDetails of ingredientDetailsViewer after get Game Details`() {
        // when
        ingredientGuessGameUiController.execute()

        // then
        verify { ingredientDetailsViewer.viewDetails(any()) }
    }

    @Test
    fun `execute should call set game of guessIngredientGameUseCase`() {
        // when
        ingredientGuessGameUiController.execute()

        verify { guessIngredientGameUseCase.guessGame(any(), any()) }

    }


}

