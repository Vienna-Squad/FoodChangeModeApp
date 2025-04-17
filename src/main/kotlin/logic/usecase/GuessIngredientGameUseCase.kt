package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.koin.core.logger.MESSAGE


class GuessIngredientGameUseCase(
    private val mealsRepository: MealsRepository
) {


    fun generateRandomMeal(): String {
        return mealsRepository.getAllMeals()
            .filter { it.numberOfIngredients != 0 }
            .map(Meal::name).random()
            ?: throw IngredientRandomMealGenerationException("The Meal Not Contain Any Ingredients")
    }

    fun generateIngredientListOptions(randomMealName: String?, randomChoose: Boolean): List<String?> {

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

    fun getIngredientOptionByNumber(ingredientsOptions: List<String?>, option: Int): String {
        return when (option) {
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
        } ?: throw IngredientsOptionsException("Ingredients List is Null Or Empty")
    }

    fun checkIngredientUserInput(ingredient: String, randomMealName: String?): Boolean {
        return mealsRepository.getAllMeals()
            .first { it.name == randomMealName }
            .ingredients
            ?.any { ingredientItem -> ingredientItem.contains(ingredient, ignoreCase = true) } == true
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

// instead Exception put App Exception
open class IngredientGameException(message: MESSAGE) : Exception(message)
class IngredientRandomMealGenerationException(message: String) : IngredientGameException(message)
class IngredientUserInputException(message: String) : IngredientGameException(message)
class IngredientsOptionsException(message: String) : IngredientGameException(message)