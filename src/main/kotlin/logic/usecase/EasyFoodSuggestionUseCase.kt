package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class EasyFoodSuggestionUseCase(private val mealsRepository: MealsRepository) {
    fun getMeals():List<Meal> {
        return emptyList()
    }

    fun isPreparedInSixStepsOrFewer(meal:Meal):Boolean{
        return false
    }
}