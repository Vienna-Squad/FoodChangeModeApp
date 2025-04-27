package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.EmptyRandomMealException
import org.example.logic.usecase.exceptions.IngredientRandomMealGenerationException
import org.example.logic.usecase.exceptions.IngredientUserInputException
import org.example.logic.usecase.exceptions.IngredientsOptionsException
import org.example.presentation.model.IngredientGameDetails


class GuessIngredientGameUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getGameDetails(): IngredientGameDetails {

        val randomMeal = getRandomMeal()
        val ingredients =
            getIngredientsByCorrectMealId(randomMeal.id!!)

        return IngredientGameDetails(
            meal = randomMeal,
            ingredients = ingredients,
        )
    }

    fun guessGame(
        ingredientGameDetails: IngredientGameDetails,
        ingredientGuessNumber: Int,
    ): Boolean {

        val guessIngredientIndex = ingredientGuessNumber - 1
        var guessIngredient: String? = null

        if (guessIngredientIndex in 0..2)
            guessIngredient = ingredientGameDetails.ingredients[guessIngredientIndex]
        else
            throw IngredientUserInputException("The Guess Number is Not In Range from 1 to 3")

        val isGuessIngredientCorrect = ingredientGameDetails.meal
            .ingredients
            ?.any { correctIngredient -> correctIngredient == guessIngredient }

        return if (isGuessIngredientCorrect == true) true
        else throw IngredientsOptionsException("Your Guess Not Correct , End Game")

    }

    fun updateScore(score: Int): Int {
        return if (score == FINAL_SCORE) FINAL_SCORE
        else score.plus(SCORE_INCREMENT)
    }


    private fun getRandomMeal(): Meal {
        return mealsRepository.getAllMeals()
            .filter(::filterNameAndIngredients)
            .ifEmpty { throw IngredientRandomMealGenerationException("The Meal list is null or empty") }
            .shuffled()
            .random()
    }

    private fun filterNameAndIngredients(meal: Meal): Boolean {
        return meal.name != null && meal.ingredients != null
    }

    private fun getIngredientsByCorrectMealId(correctMealId: Long): List<String?> {

        val correctIngredients = mealsRepository.getAllMeals()
            .ifEmpty{throw  IngredientRandomMealGenerationException("The Meal list is null or empty") }
            .first { it.id == correctMealId }
            .ingredients
            ?.random()
            ?:throw EmptyRandomMealException("The Correct Ingredients List is null or empty")

        val wrongIngredient = mealsRepository.getAllMeals()
            .takeLast(20)
            .shuffled()
            .filter { it.id != correctMealId }
            .ifEmpty{throw EmptyRandomMealException("The Wrong Ingredients List is null or empty")}
            .random()
            .ingredients
            ?.take(2)

        val ingredients = wrongIngredient
            ?.plus(correctIngredients)
            ?.shuffled()
            .takeIf { items -> items?.size == 3 }
            ?: throw EmptyRandomMealException("The size of meal is less than 3 or empty")

        return ingredients

    }


    companion object {

        const val SCORE_INCREMENT = 1000
        const val FINAL_SCORE = 15000
    }

}


