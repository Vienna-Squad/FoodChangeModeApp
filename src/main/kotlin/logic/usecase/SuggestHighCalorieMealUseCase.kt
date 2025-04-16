package org.example.logic.usecase


import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class SuggestHighCalorieMealUseCase(
    private val mealsRepository: MealsRepository
) {


    fun suggestNameAndDescriptionOfHighCalorieMeal(): Pair<String?, String?> {
        val suggestionPairMeal = mealsRepository.getAllMeals()
            .filter { it.nutrition?.calories!! > 700f }
            .map { it.name to it.description }
            .random()
        return suggestionPairMeal
    }


    fun getSuggestionHighCalorieMealDetails(mealName: String): Meal =
        mealsRepository.getAllMeals().first { it.name == mealName }


}