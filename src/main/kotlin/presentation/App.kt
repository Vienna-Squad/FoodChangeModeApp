package org.example.presentation
import org.example.logic.usecase.SearchFoodsByDateUseCase
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.MealsNotFoundForThisDateException
import org.example.utils.MenuItem
import org.example.utils.toMenuItem

class App (

    private val searchMealsByDateUseCase:SearchFoodsByDateUseCase
    ){

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
                MenuItem.MEAL_BY_DATE -> handleSearchByDate()
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

    private fun handleSearchByDate() {

        print("Enter date (dd/MM/yyyy): ")
        val inputDate = readln()

        try {
            val meals = searchMealsByDateUseCase(inputDate)
            println("Meals on $inputDate:")
            meals.forEach {meal->
                println("ID : ${meal.id}, Name : ${meal.name}")

            }

            print("Enter meal ID to view details: ")
            val id = readln().toLongOrNull()
            val meal = id?.let {id->
                meals.find {meal->
                    meal.id==id
                }
            }
            println()
            println(meal ?: "No meal found with this ID.")

        } catch (e: IncorrectDateFormatException) {
            println("${e.message} Please use dd/MM/yyyy format.")
        } catch (e: MealsNotFoundForThisDateException) {
            println(e.message)
        }
    }
}