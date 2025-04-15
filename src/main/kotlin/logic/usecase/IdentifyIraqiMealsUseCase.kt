package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class IdentifyIraqiMealsUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getMeals(): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter(::isIraqiMeal)
    }

    private fun isIraqiMeal(meal: Meal): Boolean {
        val hasIraqiTag = meal.tags?.any { it.equals("iraqi", ignoreCase = true) } ?: false
        val isDescriptionContainsIraqWord = meal.description?.contains("Iraq", ignoreCase = true) ?: false
        return hasIraqiTag || isDescriptionContainsIraqWord
    }
}