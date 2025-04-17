package org.example.presentation

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.*
import org.example.logic.usecase.exceptions.GuessPrepareTimeGameException
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.MealsNotFoundForThisDateException
import org.example.logic.usecase.exceptions.NoMealFoundByNameException
import org.example.utils.MenuItem
import org.example.utils.toMenuItem

class App(
    private val getMealByName: GetMealByName,
    private val getMealsByDateUseCase: GetMealsByDateUseCase,
    private val getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase,
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase,
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
    private val getKetoMealUseCase: GetKetoMealUseCase,
    private val getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase,
    private val getMealsByProteinAndCaloriesUseCase: GetMealsByProteinAndCaloriesUseCase,
    private val getMealsOfCountryUseCase: GetMealsOfCountryUseCase,
    private val getRankedSeafoodByProteinUseCase: GetRankedSeafoodByProteinUseCase,
    private val guessIngredientGameUseCase: GuessIngredientGameUseCase,
    private val suggestHighCalorieMealUseCase: SuggestHighCalorieMealUseCase
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
                MenuItem.KETO_DIET_MEAL -> suggestKetoMeals()
                MenuItem.MEAL_BY_DATE -> handleSearchByDate()
                MenuItem.CALCULATED_CALORIES_MEAL -> TODO()
                MenuItem.MEAL_BY_COUNTRY -> handleMealByCountry()
                MenuItem.INGREDIENT_GAME -> showIngredientGuessGame()
                MenuItem.POTATO_MEALS -> showPotatoesMeals()
                MenuItem.HIGH_CALORIES_MEAL -> showHighCalorieMeal()
                MenuItem.SEAFOOD_MEALS -> TODO()
                MenuItem.ITALIAN_MEAL_FOR_GROUPS -> TODO()
                MenuItem.EXIT -> {}
            }

        } while (selectedAction != MenuItem.EXIT)

    }

    private fun handleMealByCountry() {
        print("Enter a  country to explore its meals: ")
        val input = readln()
        try {
            val meals = getMealsOfCountryUseCase(input)
            if (meals.isEmpty()) {
                println("No meals found for '$input'.")
            } else {
                println("Meals related to '$input':")
                meals.forEachIndexed { index, meal ->
                    println("${index + 1}. ${meal.name}")
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    private fun showIngredientGuessGame() {

        // init
        var score = 0
        var counter = 0
        var randomNumber = true
        var correctGuess = true

        while (correctGuess && counter < 15) {

            val randomMealName = guessIngredientGameUseCase.generateRandomMeal()
            println("The Meal : $randomMealName")

            val showUserList = guessIngredientGameUseCase.generateIngredientListOptions(randomMealName, randomNumber)
            randomNumber = !randomNumber
            println(showUserList)
            println("Press (1) for option 1 \n\t(2) for option 2\n\t(3) for option 3")

            print("Enter the Ingredient Input Number : ")
            val input = readln().toIntOrNull() ?: -1

            val ingredientUserInput = guessIngredientGameUseCase.getIngredientOptionByNumber(showUserList, input)


            correctGuess = guessIngredientGameUseCase.checkIngredientUserInput(ingredientUserInput, randomMealName)


            if (correctGuess) {
                score = guessIngredientGameUseCase.updateScore(score)
                counter++
            } else {
                println("Your Score : $score")
                println("failure try again later ...")
                println("End Game ")
            }


        }
    }
 private  fun showHighCalorieMeal() {
        do {
            try {
                println("The Suggestion High Calorie Meal ")
                val nameAndDescription = suggestHighCalorieMealUseCase.suggestNameAndDescriptionOfHighCalorieMeal()
                suggestHighCalorieMealUseCase.checkMealIsFound(nameAndDescription)
                println("name : ${nameAndDescription.first} , Description : ${nameAndDescription.second}")
                println("\nIf you want more details about meal press (1) ")
                println("if you want another meal press (2) ")
                println("if you want To Exit (3) ")
                print("Input Your Choose Number : ")
                val inputUser = readln().toIntOrNull()

                when (inputUser) {

                    1 -> {
                        showMealDetails(suggestHighCalorieMealUseCase.getSuggestionHighCalorieMealDetails(nameAndDescription.first!!))
                        break
                    }

                    2 -> println("Another Suggestion Meal \n")

                    3 -> break

                    else -> throw InvalidInputNumberOfHighCalorieMeal("$inputUser is not in valid range (0..3) , please try again\n\n")
                }
            } catch (emptyException: EmptyRandomMealException) {
                print("Error : ${emptyException.message}")
            } catch (invalidInputException: InvalidInputNumberOfHighCalorieMeal) {
                print("Error : ${invalidInputException.message}")
            } catch (error: Exception) {
                println("Error : ${error.message}")
            }

        } while (true)
    }
    fun showMealDetails(meal: Meal){
        println("name : ${meal.name}")
        println("ingredients : ${meal.description}")
        println("minutes : ${meal.minutes}")
        println("ingredients : ${meal.ingredients}")
        println("steps : ${meal.steps}")
        showNutrition(meal.nutrition!!)
    }
    fun showNutrition(nutrition: Nutrition){
        println("calories : ${nutrition.calories}")
        println("sodium : ${nutrition.sodium}")
        println("sugar  : ${nutrition.sugar}")
        println("protein : ${nutrition.protein}")
        println("totalFat : ${nutrition.totalFat}")
        println("carbohydrates : ${nutrition.carbohydrates}")
        println("saturatedFat : ${nutrition.saturatedFat}")
    }

    private fun suggestKetoMeals() {
        val seenMeals = mutableSetOf<Meal>()
        while (true) {
            val meal = getKetoMealUseCase(seenMeals)
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
                    println("Steps: ${meal.steps}")
                    println("Nutrition Info:")
                    println("  Calories: ${meal.nutrition?.calories}")
                    println("  Fat: ${meal.nutrition?.totalFat}")
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

    private fun handleTheMealSearchByName() {
        print("Enter Name to search for a meal: ")
        val inputName = readln()
        try {
            val meal = getMealByName(inputName)
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
            val meals = getMealsByDateUseCase(inputDate)
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
