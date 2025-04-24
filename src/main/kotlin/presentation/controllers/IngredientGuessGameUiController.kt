package org.example.presentation.controllers

import org.example.logic.usecase.GuessIngredientGameUseCase
import org.example.logic.usecase.IngredientGameDetails
import org.example.logic.usecase.exceptions.IngredientsOptionsException
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.IngredientGameDetailsViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.utils.viewer.IngredientGameScoreViewer

import org.koin.mp.KoinPlatform.getKoin

class IngredientGuessGameUiController(
    private val guessIngredientGameUseCase: GuessIngredientGameUseCase = getKoin().get(),
    private val ingredientDetailsViewer: ItemDetailsViewer<IngredientGameDetails> = IngredientGameDetailsViewer(),
    private val scoreViewer: IngredientGameScoreViewer = IngredientGameScoreViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer()
) : UiController {
    override fun execute() {

        try {
            var counter = 1
            do {
                val gameDetails = guessIngredientGameUseCase.getGameDetails()
                ingredientDetailsViewer.viewDetails(gameDetails)

                print("Enter the Ingredient Input Number : ")
                val input = readln().toIntOrNull() ?: -1
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
        }
        guessIngredientGameUseCase.endGame()
    }
}