package org.example.presentation

import org.example.logic.usecase.GuessIngredientGameUseCase
import org.example.utils.MenuItem
import org.example.utils.toMenuItem

class App(
    val guessIngredientGameUseCase: GuessIngredientGameUseCase
) {

    fun start() {
        do {
            MenuItem.entries.forEachIndexed { index, action ->
                println("${index + 1}- ${action.title}")
            }
            print("choose the action \u001B[33m*enter (8) or anything else to exit*\u001B[0m: ")
            val selectedAction = (readln().toIntOrNull() ?: -1).toMenuItem()
            println()
            when (selectedAction) {
                MenuItem.HEALTHY_FAST_FOOD -> TODO()
                MenuItem.MEAL_BY_NAME -> TODO()
                MenuItem.IRAQI_MEALS -> TODO()
                MenuItem.EASY_FOOD_SUGGESTION_GAME -> TODO()
                MenuItem.PREPARATION_TIME_GUESSING_GAME -> TODO()
                MenuItem.EGG_FREE_SWEETS -> TODO()
                MenuItem.KETO_DIET_MEAL -> TODO()
                MenuItem.MEAL_BY_DATE -> TODO()
                MenuItem.CALCULATED_CALORIES_MEAL -> TODO()
                MenuItem.MEAL_BY_COUNTRY -> TODO()
                MenuItem.INGREDIENT_GAME -> showIngredientGuessGame(guessIngredientGameUseCase)
                MenuItem.POTATO_MEALS -> TODO()
                MenuItem.FOR_THIN_MEAL -> TODO()
                MenuItem.SEAFOOD_MEALS -> TODO()
                MenuItem.ITALIAN_MEAL_FOR_GROUPS -> TODO()
                MenuItem.EXIT -> TODO()
            }

        } while (selectedAction != MenuItem.EXIT)

    }

    private fun showIngredientGuessGame(guess: GuessIngredientGameUseCase) {
        var score = 0
        var counter = 0
        var randomNumber = true
        var correctGuess = true

        while (correctGuess && counter < 15) {

            val randomMealName = guess.generateRandomMeal()
            println("The Meal : $randomMealName")

            val showUserList = guess.generateIngredientListOptions(randomMealName, randomNumber)
            randomNumber = !randomNumber
            println(showUserList)
            println("Press (1) for option 1 \n\t(2) for option 2\n\t(3) for option 3")

            // get User ingredient
            print("Enter the Ingredient Input Number : ")
            val input = readln().toIntOrNull()

            val getUserInput = guess.getIngredientOptionByNumber(showUserList, input?:-1)

            if (guess.checkIngredientUserInput(getUserInput, randomMealName)) {
                println("Correct .....")
                score += 1000
                counter++
            } else {
                println("Your Score : $score")
                println("failure try again later ...")
                println("End Game ")
                correctGuess = false
            }

        }
    }
}