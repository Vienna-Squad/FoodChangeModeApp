package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class SuggestKetoMealUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getMeal(seenMeals: Set<Meal>): Meal? {
        return mealsRepository.getAllMeals()
            .filter { isKetoFriendlyMeal(it) && it !in seenMeals }
            .randomOrNull()
    }

    private fun isKetoFriendlyMeal(meal: Meal): Boolean {

        val nutrition = meal.nutrition ?: return false
        val totalFat = nutrition.totalFatL ?: return false
        val carbs = nutrition.carbohydrates ?: return false

        return totalFat >= 20.0 && carbs <= 20.0
    }
}
