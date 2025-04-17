package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class GetKetoMealUseCase(private val mealsRepository: MealsRepository) {
    operator fun invoke(seenMeals: Set<Meal>) = mealsRepository.getAllMeals()
        .filter { isKetoFriendlyMeal(it) && it !in seenMeals }
        .randomOrNull()

    private fun isKetoFriendlyMeal(meal: Meal): Boolean {
        val nutrition = meal.nutrition ?: return false
        val totalFat = nutrition.totalFat ?: return false
        val carbs = nutrition.carbohydrates ?: return false
        return totalFat >= 20.0 && carbs <= 20.0
    }
}
