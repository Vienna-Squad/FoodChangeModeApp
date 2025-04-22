package org.example.presentation

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.*
import org.example.logic.usecase.exceptions.*
import org.example.utils.MenuItem
import org.example.utils.show
import org.example.utils.toMenuItem

class App(
    private val getMealByName: GetMealByName,
    private val getMealsByDateUseCase: GetMealsByDateUseCase,
    private val getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase,
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase,
    private val guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase,
    private val getEggFreeSweetsUseCase: GetEggFreeSweetsUseCase,
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
    private val getKetoMealUseCase: GetKetoMealUseCase,
    private val getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase,
    private val getMealsByProteinAndCaloriesUseCase: GetMealsByProteinAndCaloriesUseCase,
    private val getMealsOfCountryUseCase: GetMealsOfCountryUseCase,
    private val getRankedSeafoodByProteinUseCase: GetRankedSeafoodByProteinUseCase,
    private val guessIngredientGameUseCase: GuessIngredientGameUseCase,
    private val getHighCalorieMealUseCase: GetHighCalorieMealUseCase,
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase
) {
    fun start() {

        do {
             displayMenu()
            val selectedAction = (readln().toIntOrNull() ?: -1).toMenuItem()
            println()
            when (selectedAction) {
                MenuItem.HEALTHY_FAST_FOOD -> findHealthyFastFood()
                MenuItem.MEAL_BY_NAME -> handleTheMealSearchByName()
                MenuItem.IRAQI_MEALS -> showIraqiMeals()
                MenuItem.EASY_FOOD_SUGGESTION_GAME -> showEasyMeal()
                MenuItem.PREPARATION_TIME_GUESSING_GAME -> startPreparationTimeGuessingGame()
                MenuItem.EGG_FREE_SWEETS -> showEggFreeSweets()
                MenuItem.KETO_DIET_MEAL -> suggestKetoMeals()
                MenuItem.MEAL_BY_DATE -> handleSearchByDate()
                MenuItem.CALCULATED_CALORIES_MEAL -> executeGetMealsByProteinAndCalories()
                MenuItem.MEAL_BY_COUNTRY -> handleMealByCountry()
                MenuItem.INGREDIENT_GAME -> showIngredientGuessGame()
                MenuItem.POTATO_MEALS -> showPotatoesMeals()
                MenuItem.HIGH_CALORIES_MEAL -> showHighCalorieMeal()
                MenuItem.SEAFOOD_MEALS -> executeGetRankedSeafoodByProtein()
                MenuItem.ITALIAN_MEAL_FOR_GROUPS -> handleItalianMealForGroups()
                MenuItem.EXIT -> println("See you later!!")
            }

        } while (selectedAction != MenuItem.EXIT)
    }

        private fun findHealthyFastFood() {
            try {
                val healthyFastFoodMeals = getHealthyFastFoodUseCase()
                if (healthyFastFoodMeals.isNotEmpty()) {
                    println("================== Healthy Fast Food Suggestions ==================")
                    healthyFastFoodMeals.forEach { println("- ${it.name}") }
                } else {
                    println("No healthy fast food options found based on the criteria.")
                }
            } catch (e: Exception) {
                println("Error fetching healthy fast food: ${e.message}")
            }
        }

        private fun executeGetMealsByProteinAndCalories() {
            println("--- Find Meals by Calories & Protein ---")
            print("Enter desired calories (e.g., 500): ")
            val caloriesInput = readln().toFloatOrNull()
            print("Enter desired protein in grams (e.g., 30): ")
            val proteinInput = readln().toFloatOrNull()

            if (caloriesInput == null || proteinInput == null) {
                println("\u001B[31mInvalid input. Please enter numbers only for calories and protein.\u001B[0m")
                return
            }

            val results = getMealsByProteinAndCaloriesUseCase(caloriesInput, proteinInput)

            if (results.isEmpty()) {
                println("\u001B[33mNo meals found matching your criteria within the tolerance.\u001B[0m")
            } else {
                println("\u001B[32mFound ${results.size} matching meals:\u001B[0m")
                results.forEach { meal ->
                    // Display meal name and relevant nutrition
                    val caloriesStr = meal.nutrition?.calories?.toString() ?: "N/A"
                    val proteinStr = meal.nutrition?.protein?.toString() ?: "N/A"
                    println("- ${meal.name ?: "Unnamed Meal"} (Calories: $caloriesStr, Protein: ${proteinStr}g)")
                }
            }
        }

        private fun executeGetRankedSeafoodByProtein() {
            println("--- Seafood Meals Sorted by Protein (Highest First) ---")

            val results: List<RankedMealResult> = getRankedSeafoodByProteinUseCase()

            if (results.isEmpty()) {
                println("\u001B[33mNo seafood meals with protein information found.\u001B[0m")
            } else {
                // Green table header
                println("\u001B[32mRank | Meal Name                      | Protein (g)\u001B[0m")
                println("-----|--------------------------------|------------")
                results.forEach { rankedMeal ->
                    // Format output for alignment
                    val rankStr = rankedMeal.rank.toString().padEnd(4)
                    val nameStr = (rankedMeal.name ?: "Unnamed Meal").take(30).padEnd(30) // Truncate long names
                    val proteinStr = rankedMeal.protein?.toString() ?: "N/A"
                    println("$rankStr | $nameStr | $proteinStr")
                }
            }
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

        private fun showEggFreeSweets() {
            val seenMeals = mutableSetOf<Meal>()
            while (true) {
                val meal = getEggFreeSweetsUseCase(seenMeals)
                if (meal == null) {
                    println("There Is no Egg-Free sweets")
                    break
                }
                println("Meal: ${meal.name}")
                println(meal.description ?: "No description available.")
                println("1 - Like    |   2 - Dislike ")
                print("Your choice: ")
                when (readlnOrNull()) {
                    "1" -> {
                        println("\n Full Details of ${meal.name}")
                        println("Preparation Time: ${meal.minutes} ")
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
                        println("Exiting Egg-Free Sweets Suggestions")
                        break
                    }
                }
            }
        }

        private fun handleItalianMealForGroups() {
            try {
                val meals = getItalianGroupMealsUseCase()
                meals.forEach { meal ->
                    println(meal.name)
                }
            } catch (e: Exception) {
                println("No Italian meals for groups found")
            }
        }


        private fun showIngredientGuessGame() {

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

        private fun showHighCalorieMeal() {
            try {
                do {
                    println("The Suggestion High Calorie Meal ")
                    getHighCalorieMealUseCase.getNameAndDescription().show()
                    println("\t(1) Like (show more details about meal)")
                    println("\t(2) Dislike (show another meal )")
                    println("\t(3) Exit ")
                    print("Input Your Choose Number : ")
                    val inputUser = readln().toIntOrNull()

                    when (inputUser) {

                        1 -> {
                            showMealDetails(
                                getHighCalorieMealUseCase.getMealDetailsByName()
                            )
                            break
                        }

                        2 -> println("Get Another Suggestion Meal \n")

                        3 -> break

                        else -> throw InvalidInputNumberOfHighCalorieMeal("$inputUser is not in valid range (0..3) , please try again\n\n")
                    }

            } while (true)
            } catch (emptyException: NullRandomMealException) {
                print("Error : ${emptyException.message}")
            } catch (exception:NullHighCalorieRandomMealException){
                print("Error : ${exception.message}")
            } catch (invalidInputException: InvalidInputNumberOfHighCalorieMeal) {
                print("Error : ${invalidInputException.message}")
            } catch (error: Exception) {
                println("Error : ${error.message}")
            }

        }

        private fun showMealDetails(meal: Meal) {
            println("name : ${meal.name}")
            println("ingredients : ${meal.description}")
            println("minutes : ${meal.minutes}")
            println("ingredients : ${meal.ingredients}")
            println("steps : ${meal.steps}")
            showNutrition(meal.nutrition!!)
        }

        private fun showNutrition(nutrition: Nutrition) {
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
                    showMealDetails(meal)
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

    private fun displayMenu() {

        MenuItem.entries.forEachIndexed { index, option ->
            when (index) {
                0 -> printCategoryHeader(" General Meal Discovery")
                6 -> printCategoryHeader(" Dietary & Nutrition")
                11 -> printCategoryHeader(" Ingredient Specials")
                13 -> printCategoryHeader(" Fun Activities")
                16 -> printCategoryHeader(" System")
            }
            val number = (index + 1).toString().padEnd(2)
            println("  \u001B[32m$number.\u001B[0m ${option.title}")
        }
        println("\n\u001B[33mEnter your choice (1-${MenuItem.entries.size}): \u001B[0m")
        print("\u001B[33m*Enter 16 or Anything Else to Exit*\u001B[0m:")

    }

    private fun printCategoryHeader(title: String) {
        println("\n\u001B[1;33m$title\u001B[0m")
        println("\u001B[37m${"-".repeat(title.length)}\u001B[0m")
    }

}
