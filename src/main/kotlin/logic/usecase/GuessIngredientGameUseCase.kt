package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.EmptyRandomMealException
import org.example.logic.usecase.exceptions.IngredientRandomMealGenerationException
import org.example.logic.usecase.exceptions.IngredientUserInputException
import org.example.logic.usecase.exceptions.IngredientsOptionsException
import org.example.logic.usecase.exceptions.NoMealFoundException


class GuessIngredientGameUseCase(
    private val mealsRepository: MealsRepository
) {

    private var score = 0

    fun getGameDetails(): IngredientGameDetails {

        val randomMeal = getRandomMeal()
        val ingredients = getIngredientsByCorrectMealId(randomMeal.id!!)

        return IngredientGameDetails(
            meal = randomMeal,
            ingredients = ingredients,
        )
    }

    fun setGame(ingredientGameDetails: IngredientGameDetails, ingredientInputNumber: Int) {

        val ingredientsOptions = ingredientGameDetails.ingredients

        val ingredient = getChosenIngredientByOptionNumber(
            ingredientsOptions = ingredientsOptions,
            ingredientNumberOption = ingredientInputNumber
        )

        val meal = ingredientGameDetails.meal
        val isCorrectResult = isMealContainsIngredient(
            correctMeal = meal,
            resultIngredient = ingredient
        )

        if (isCorrectResult) {
            updateScore()
            if (this.score == FINAL_SCORE)
                return
        }

    }

    fun getResultOfGame(): IngredientGameResult {
        val result = IngredientGameResult(
            score = score,
        )
        return result
    }

    fun endGame() {
        resetScore()
    }


    private fun getFilterdMeals(): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter { it.name != null && it.ingredients != null }
            .take(20)
            .ifEmpty { throw IngredientRandomMealGenerationException("The Meal list is null or empty")}
    }

    private fun getRandomMeal(): Meal {
        return getFilterdMeals().randomOrNull() ?: throw IngredientRandomMealGenerationException("The Meal is null or empty")
    }

    private fun getIngredientsByCorrectMealId(correctMealId: Long): List<String?> {

        val correctIngredients = getFilterdMeals()
            .firstOrNull { it.id == correctMealId }
            ?.ingredients
            ?.take(2)
            ?.ifEmpty { throw EmptyRandomMealException("The Correct Ingredients List is null or empty") }

        val wrongIngredient = mealsRepository.getAllMeals()
            .firstOrNull { it.id != correctMealId }
            ?.ingredients
            ?.randomOrNull()
            ?.ifEmpty { throw EmptyRandomMealException("The Wrong Ingredients List is null or empty") }

        val ingredients = correctIngredients
            ?.plus(wrongIngredient)
            ?.sortedBy { ingredient -> ingredient?.length }

        return ingredients ?: throw EmptyRandomMealException("The All Ingredients Options List is null or empty")

    }

    private fun getChosenIngredientByOptionNumber(ingredientsOptions: List<String?>, ingredientNumberOption: Int): String {
        return when (ingredientNumberOption) {
            FIRST_OPTION -> {
                ingredientsOptions[FIRST_OPTION_INDEX]
            }

            SECOND_OPTION -> {
                ingredientsOptions[SECOND_OPTION_INDEX]
            }

            THIRD_OPTION -> {
                ingredientsOptions[THIRD_OPTION_INDEX]
            }

            else -> {
                throw IngredientUserInputException("InValid Input Number")
            }
        } ?: throw IngredientsOptionsException("Something Wrong Happened")
    }

    private fun isMealContainsIngredient(
        correctMeal: Meal,
        resultIngredient: String
    ): Boolean {
        val check = getFilterdMeals()
            .firstOrNull { it == correctMeal }
            ?.ingredients
            ?.any { ingredientItem -> ingredientItem.contains(resultIngredient, ignoreCase = true) } == true

        if (check)
            return true
        else
            throw IngredientsOptionsException("Your Answer Not Correct End Game")
    }

    private fun updateScore() {
        this.score += SCORE_INCREMENT
    }


    private fun resetScore() {
        this.score = 0
    }

    companion object {

        const val FIRST_OPTION = 1
        const val SECOND_OPTION = 2
        const val THIRD_OPTION = 3

        const val FIRST_OPTION_INDEX = 0
        const val SECOND_OPTION_INDEX = 1
        const val THIRD_OPTION_INDEX = 2

        const val SCORE_INCREMENT = 1000
        const val FINAL_SCORE = 15000
    }

}

data class IngredientGameDetails(
    val meal: Meal,
    val ingredients: List<String?>,
)

data class IngredientGameResult(
    val score: Int,
)