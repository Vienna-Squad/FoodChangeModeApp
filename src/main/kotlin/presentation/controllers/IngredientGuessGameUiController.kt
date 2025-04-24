package org.example.presentation.controllers

import org.example.logic.usecase.GuessIngredientGameUseCase
import org.example.logic.usecase.exceptions.IngredientsOptionsException
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class IngredientGuessGameUiController(
    private val guessIngredientGameUseCase: GuessIngredientGameUseCase = getKoin().get(),
    private val viewer: FoodViewer = FoodViewer()
) : UiController {
    override fun execute() {

        try {
            var counter = 1
            do {
                val gameDetails = guessIngredientGameUseCase.getGameDetails()
//                viewer.showMealDetails(gameDetails.meal)
                println(gameDetails.mealName)
                println("\nOptions : ${gameDetails.ingredients}\n")
                println("\t(1) first Option")
                println("\t(2) second Option")
                println("\t(3) third Option\n")

                print("Enter the Ingredient Input Number : ")
                val input = readln().toIntOrNull() ?: -1
                guessIngredientGameUseCase.setGame(
                    ingredientGameDetails = gameDetails,
                    ingredientInputNumber = input
                )

                val result = guessIngredientGameUseCase.getScoreOfGame()
                println("Score : $result , CorrectResults : $counter")
                counter++

            } while (counter <= 15)
        } catch (e: IngredientsOptionsException) {
            viewer.showExceptionMessage(e)
            val score = guessIngredientGameUseCase.getScoreOfGame()
            println("Score : $score ")
        }
        guessIngredientGameUseCase.endGame()
    }
}