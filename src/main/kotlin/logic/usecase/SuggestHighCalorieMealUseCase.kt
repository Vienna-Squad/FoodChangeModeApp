package org.example.logic.usecase


import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundException


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

    private fun filterNameAndDescription(meal: Meal): Boolean {
        return meal.name !== null && meal.description != null
    }

    private fun filterMealByHighCalorie(meal: Meal): Boolean {
        return (meal.nutrition?.calories ?: 0f) > 700f
    }


    fun getSuggestionHighCalorieMealDetails(mealName: String): Meal =
        mealsRepository.getAllMeals().first { it.name == mealName }

    fun checkMealIsFound(nameAndDescription: Pair<String?, String?>): Boolean {
        return if (nameAndDescription.first.isNullOrEmpty() && nameAndDescription.second.isNullOrEmpty())
            throw NoMealFoundException("The High Calorie Meal Is Not Found")
        else
            true
    }
}