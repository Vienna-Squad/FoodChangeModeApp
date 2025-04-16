package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.koin.core.logger.MESSAGE


class GuessIngredientGameUseCase(
    private val mealsRepository: MealsRepository
) {


//    fun guess(guess: String){
//        if (!checkUserInput(guess,generateRandomMeal())) print("Wrong Answer")
//        else{
//            guess(guess)
//        }
//    }

    private fun generateRandomMeal(): String {
        return mealsRepository.getAllMeals().map(Meal::name).random()
            ?: throw Exception("error in generate Random Meal")
    }


    private fun checkUserInput(ingredient: String, randomMealName: String?): Boolean {
        return mealsRepository.getAllMeals()
            .first { it.name == randomMealName }
            .ingredients?.any { ingredientItem -> ingredientItem.contains(ingredient, ignoreCase = true) } == true
    }

    private fun getIngredientListOptions(randomMealName: String?, randomChoose: Boolean): List<String?> {

        val correctOptions = mealsRepository.getAllMeals()
            .first { it.name == randomMealName }
            .ingredients

        val firstFalseOption = mealsRepository.getAllMeals()
            .filter { it.name != randomMealName }
            .map(Meal::ingredients)
            .random()?.random()

        val secondFalseOption = mealsRepository.getAllMeals()
            .filter { it.name != randomMealName }
            .map(Meal::ingredients)
            .random()?.random()

        return when (randomChoose) {
            true -> listOf(
                correctOptions?.get(FIRST_CORRECT_INDEX),
                firstFalseOption,
                secondFalseOption
            ).sortedBy { it?.length }

            false -> listOf(
                correctOptions?.get(FIRST_CORRECT_INDEX),
                firstFalseOption,
                correctOptions?.get(SECOND_CORRECT_INDEX),
            ).sortedBy { it?.length }
        }
    }

    fun showIngredientGuessGame(){

        var score = 0
        var counter = 0
        var randomNumber = true
        var correctGuess = true

        while (correctGuess && counter < 15) {


            val randomMealName = generateRandomMeal()
            println("The Meal : $randomMealName")

            val showUserList = getIngredientListOptions(randomMealName, randomNumber)
            randomNumber = !randomNumber
            println(showUserList)
            println("1 for option 1 ### 2 for option 2 ### 3 for option 3")

            // get User ingredient
            print("Enter the Ingredient Input Number : ")
            val input = readln().toIntOrNull()

            val getUserInput = getOptionFromIngredientsList(showUserList, input?:-1)

            if (checkUserInput(getUserInput, randomMealName)) {
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
    private fun getOptionFromIngredientsList(ingredients: List<String?>, option: Int): String {
        return when (option) {
            FIRST_OPTION -> {
                ingredients[FIRST_OPTION_INDEX]
            }

            SECOND_OPTION -> {
                ingredients[SECOND_OPTION_INDEX]
            }

            THIRD_OPTION -> {
                ingredients[THIRD_OPTION_INDEX]
            }

            else -> {
                throw Exception("InValid Number")
            }
        } ?: throw Exception("Ingredients List is Null Or Empty")
    }

    companion object {
        const val FIRST_CORRECT_INDEX = 0
        const val SECOND_CORRECT_INDEX = 2

        const val FIRST_OPTION = 1
        const val SECOND_OPTION = 2
        const val THIRD_OPTION = 3

        const val FIRST_OPTION_INDEX = 0
        const val SECOND_OPTION_INDEX = 1
        const val THIRD_OPTION_INDEX = 2
    }

}

open class IngredientGameException(message: MESSAGE): Exception(message)
//error in generate Random Meal
