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

        val randomMeal = mealsRepository.getAllMeals()
            .filter(::filterNameAndIngredients)
            .ifEmpty { throw IngredientRandomMealGenerationException("The Meal list is null or empty") }
            .shuffled()
            .random()

        val ingredientOfRandomMeal = randomMeal.ingredients!!.first()

        val ingredients = mealsRepository.getAllMeals()
            .filter { meal -> meal.id != randomMeal.id && filterNameAndIngredients(meal) }
            .ifEmpty { throw EmptyRandomMealException("The Wrong Ingredients List is null or empty") }
            .random()
            .ingredients!!
            .take(2)
            .plus(ingredientOfRandomMeal)
            .shuffled()


        return IngredientGameDetails(
            meal = randomMeal,
            ingredients = ingredients,
        )
    }

    private fun filterNameAndIngredients(meal: Meal): Boolean {
        val check = meal.name != null && meal.ingredients != null == true
        return if (check)
            true
        else
            throw EmptyRandomMealException("The Name And Ingredients List is null or empty")
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


    companion object {

        const val SCORE_INCREMENT = 1000
        const val FINAL_SCORE = 15000
    }

}


