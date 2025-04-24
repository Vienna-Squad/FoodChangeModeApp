package org.example.presentation.controllers

import org.example.logic.usecase.GuessIngredientGameUseCase
import org.koin.mp.KoinPlatform.getKoin

class IngredientGuessGameUiController(
    private val guessIngredientGameUseCase: GuessIngredientGameUseCase = getKoin().get()
) : UiController {
    override fun execute() {
        // init
        var score = 0
        var counter = 0
        var randomNumber = true
        var correctGuess = true

        while (correctGuess && counter < 15) {

            val randomMeal = guessIngredientGameUseCase.generateRandomMeal()
            println("The Meal : ${randomMeal}\n")

            val showUserList =
                guessIngredientGameUseCase.generateIngredientListOptions(randomMeal.name, randomNumber)
            randomNumber = !randomNumber
            println("$showUserList\n")
            println("\tPress (1) for option 1 \n\tPress (2) for option 2 \n\tPress (3) for option 3\n")

            print("Enter the Ingredient Input Number : ")
            val input = readln().toIntOrNull() ?: -1

            val ingredientUserInput = guessIngredientGameUseCase.getIngredientOptionByNumber(showUserList, input)


            correctGuess = guessIngredientGameUseCase.checkIngredientUserInput(ingredientUserInput, randomMeal.name)


            if (correctGuess) {
                score = guessIngredientGameUseCase.updateScore(score)
                counter++
            } else {
                println("failure try again later ...")
                println("Your Score : $score")
                println("End Game \n\n")
            }
        }
    }
}