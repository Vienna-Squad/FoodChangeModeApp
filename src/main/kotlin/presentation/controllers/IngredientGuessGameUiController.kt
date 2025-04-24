package org.example.presentation.controllers

import org.example.logic.usecase.GuessIngredientGameUseCase
import org.example.logic.usecase.IngredientGameDetails
import org.example.logic.usecase.exceptions.IngredientUserInputException
import org.example.logic.usecase.exceptions.IngredientsOptionsException
import org.example.utils.interactor.Interactor
import org.example.utils.interactor.InteractorNumber
import org.example.utils.interactor.UserInteractor
import org.example.utils.interactor.UserInteractorNumber
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.IngredientGameDetailsViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.utils.viewer.IngredientGameScoreViewer

import org.koin.mp.KoinPlatform.getKoin

class IngredientGuessGameUiController(
    private val guessIngredientGameUseCase: GuessIngredientGameUseCase = getKoin().get(),
    private val ingredientDetailsViewer: ItemDetailsViewer<IngredientGameDetails> = IngredientGameDetailsViewer(),
    private val scoreViewer: ItemDetailsViewer<Int> = IngredientGameScoreViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer(),
    private val interactorNumber: InteractorNumber = UserInteractorNumber()
) : UiController {
    override fun execute() {

        try {
            var counter = 1
            do {
                val gameDetails = guessIngredientGameUseCase.getGameDetails()
                ingredientDetailsViewer.viewDetails(gameDetails)

                print(USER_INPUT_MESSAGE)
                val input = interactorNumber.getInput()

                guessIngredientGameUseCase.setGame(
                    ingredientGameDetails = gameDetails,
                    ingredientInputNumber = input
                )

                val score = guessIngredientGameUseCase.getScoreOfGame()
                scoreViewer.viewDetails(score)

                counter++

            } while (counter <= 15)
        } catch (e: IngredientsOptionsException) {
            val score = guessIngredientGameUseCase.getScoreOfGame()
            exceptionViewer.viewExceptionMessage(e)
            scoreViewer.viewDetails(score)
        } catch (e: IngredientUserInputException) {
            val score = guessIngredientGameUseCase.getScoreOfGame()
            exceptionViewer.viewExceptionMessage(e)
            scoreViewer.viewDetails(score)
        }finally {
            guessIngredientGameUseCase.endGame()
        }

    }

    companion object {
        const val USER_INPUT_MESSAGE = "Enter the Ingredient Input Number : "
    }
}