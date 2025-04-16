package org.example.presentation

import org.example.logic.model.Meal
import org.example.logic.usecase.SuggestKetoMealUseCase
import org.example.utils.MenuItem
import org.example.utils.toMenuItem

class App(
private val suggestKetoMealUseCase: SuggestKetoMealUseCase
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
                MenuItem.INGREDIENT_GAME -> TODO()
                MenuItem.POTATO_MEALS -> TODO()
                MenuItem.FOR_THIN_MEAL -> TODO()
                MenuItem.SEAFOOD_MEALS -> TODO()
                MenuItem.ITALIAN_MEAL_FOR_GROUPS -> TODO()
                MenuItem.EXIT -> TODO()
            }

        } while (selectedAction != MenuItem.EXIT)

    }

    private fun suggestKetoMeals() {
    val seenMeals = mutableSetOf<Meal>()
    while (true) {
        val meal = suggestKetoMealUseCase.getMeal(seenMeals)

        if (meal == null) {
            println(" You've seen all keto meals")
            break
        }

        println("Meal: ${meal.name}")
        println(meal.description ?: "No description available.")
        println("1 - Like    |   2 - Dislike ")
        print("Your choice: ")

        when (readlnOrNull()) {
            "1" -> {
                println("\n Full Details of ${meal.name}")
                println("Time: ${meal.minutes} ")
                println("Ingredients: ${meal.ingredients}")
                println("Steps: ${meal.steps }")
                println("Nutrition Info:")
                println("  Calories: ${meal.nutrition?.calories}")
                println("  Fat: ${meal.nutrition?.totalFatL}")
                println("  Carbs: ${meal.nutrition?.carbohydrates}")
                println("  Protein: ${meal.nutrition?.protein}")
                break
            }

            "2" -> {
                seenMeals.add(meal)
                println("\n try another one\n")
            }

            else -> {
                println("Exiting Keto Meal Suggestions")
                break
            }
        }
    }
}

}