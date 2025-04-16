package org.example.presentation

import org.example.utils.MenuItem
import org.example.utils.toMenuItem

class App(

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
}