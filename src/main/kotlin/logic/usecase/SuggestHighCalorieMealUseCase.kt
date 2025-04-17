package org.example.logic.usecase


import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository


class SuggestHighCalorieMealUseCase(
    private val mealsRepository: MealsRepository
) {


    fun suggestNameAndDescriptionOfHighCalorieMeal(): Pair<String?, String?> {
        val suggestionPairMeal = mealsRepository.getAllMeals()
            .filter(::filterNameAndDescription)
            .filter(::filterMealByHighCalorie)
            .map { it.name to it.description }
            .random()
        return suggestionPairMeal
    }

    private fun filterNameAndDescription(meal: Meal): Boolean{
        return  meal.name!==null && meal.description!=null
    }

    private fun filterMealByHighCalorie(meal: Meal): Boolean{
        return  meal.nutrition?.calories!! > 700f
    }


    fun getSuggestionHighCalorieMealDetails(mealName: String): Meal =
        mealsRepository.getAllMeals().first { it.name == mealName }

    fun checkMealIsFound(nameAndDescription: Pair<String?,String?>): Boolean{
        return if (nameAndDescription.first.isNullOrEmpty()&&nameAndDescription.second.isNullOrEmpty())
            throw EmptyRandomMealException("The High Calorie Meal Is Not Found")
        else
            true
    }
}

open class HighCalorieMealException(message: String): Exception(message)
class EmptyRandomMealException(message: String): HighCalorieMealException(message)
class InvalidInputNumberOfHighCalorieMeal(message: String): HighCalorieMealException(message)