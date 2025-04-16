package org.example.presentation

import org.example.logic.usecase.ExploreCountryMealsUseCase
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.MealsNotFoundForThisDateException
import org.example.logic.usecase.*
import org.example.utils.MenuItem
import org.example.utils.toMenuItem
import org.example.logic.usecase.exceptions.GuessPrepareTimeGameException
import org.example.logic.usecase.exceptions.NoMealFoundByNameException

class App (
  private val exploreCountryMealsUseCase: ExploreCountryMealsUseCase,
    private val searchByNameUseCase: SearchForMealByName,
    private val searchMealsByDateUseCase: SearchFoodsByDateUseCase,
    private val getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase,
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase,
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
    private val findMealsByProteinAndCaloriesUseCase: FindMealsByProteinAndCaloriesUseCase,
    private val getRankedSeafoodByProteinUseCase: GetRankedSeafoodByProteinUseCase,
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
                MenuItem.MEAL_BY_NAME -> handleTheMealSearchByName()
                MenuItem.EASY_FOOD_SUGGESTION_GAME -> showEasyMeal()
                MenuItem.IRAQI_MEALS -> showIraqiMeals()
                MenuItem.PREPARATION_TIME_GUESSING_GAME -> startPreparationTimeGuessingGame()
                MenuItem.EGG_FREE_SWEETS -> TODO()
                MenuItem.KETO_DIET_MEAL -> TODO()
                MenuItem.MEAL_BY_DATE -> handleSearchByDate()
                MenuItem.CALCULATED_CALORIES_MEAL -> TODO()
                MenuItem.MEAL_BY_COUNTRY -> handleMealByCountry()
                MenuItem.INGREDIENT_GAME -> TODO()
                MenuItem.POTATO_MEALS -> showPotatoesMeals()
                MenuItem.FOR_THIN_MEAL -> TODO()
                MenuItem.SEAFOOD_MEALS -> TODO()
                MenuItem.ITALIAN_MEAL_FOR_GROUPS -> TODO()
                MenuItem.EXIT -> {}
            }

        } while (selectedAction != MenuItem.EXIT)

    }
    private fun handleMealByCountry() {
        print("Enter the country name to explore its meals: ")
        val countryInput = readln()
        try {
            val meals = exploreCountryMealsUseCase.exploreMealsByCountry(countryInput)
            if (meals.isEmpty()) {
                println("No meals found for '$countryInput'.")
            } else {
                println("Meals from '$countryInput':")
                meals.forEachIndexed { index, meal ->
                    println("${index + 1}. ${meal.name}")
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    private fun handleTheMealSearchByName() {
        print("Enter Name to search for a meal: ")
        val inputName = readln()
        try {
            val meal = searchByNameUseCase(inputName)

            println("\n Meal found:")
            println(meal)
        } catch (e: NoMealFoundByNameException) {
            println(e.message)
        }
    }

    private fun handleSearchByDate() {
        print("Enter date (dd/MM/yyyy): ")
        val inputDate = readln()
        try {
            val meals = searchMealsByDateUseCase(inputDate)
            println("Meals on $inputDate:")
            meals.forEach { meal ->
                println("ID : ${meal.id}, Name : ${meal.name}")

            }

            print("Enter meal ID to view details: ")
            val meal = readln().toLongOrNull()?.let { id ->
                meals.find { meal ->
                    meal.id == id
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

    private fun showEasyMeal() {
        val meals = getEasyFoodSuggestionUseCase()
        if (meals.isEmpty()) {
            println("No meals found")
        } else {
            println("Easy meals:")
            meals.forEach { meal ->
                println(meal)
            }
        }
    }

    private fun showIraqiMeals() {
        getIraqiMealsUseCase().let { meals ->
            if (meals.isEmpty()) {
                println("IraqiMealsNotFound")
            } else {
                meals.forEach { println(it) }
            }
        }
    }

    private fun startPreparationTimeGuessingGame() {
        with(guessPrepareTimeGameUseCase.getMeal()) {
            minutes?.let { minutes ->
                var attempt = 3
                print("guess its preparation time of $name: ")
                while (true) {
                    val guessMinutes = readln().toLongOrNull() ?: -1
                    try {
                        println(guessPrepareTimeGameUseCase.guess(guessMinutes, minutes, attempt))
                        break
                    } catch (exception: GuessPrepareTimeGameException) {
                        attempt = exception.attempt
                        print("${exception.message} try again: ")
                    }
                }
            }
        }
    }

    private fun showPotatoesMeals() {
        getRandomPotatoMealsUseCase().forEach { meal ->
            println(" Name: ${meal.name}")
            println(" Ingredients: ${meal.ingredients ?: "No ingredients listed"}")
            println("------------------------------------------------------------")
        }
    }
}
