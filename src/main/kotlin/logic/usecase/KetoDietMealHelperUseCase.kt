package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class KetoDietMealHelperUseCase(
    private val mealsRepository: MealsRepository
) {
    private val seenMeals = mutableSetOf<Meal>()

    fun getMeal(): Meal {
        val availableMeals = mealsRepository.getAllMeals()
            .filterNot { it in seenMeals }
            .filter(::isKetoFriendlyMeal)

        val meal = availableMeals.randomOrNull() ?: throw Exception("No keto meals left")

        seenMeals.add(meal)
        return meal
    }

    private fun isKetoFriendlyMeal(meal: Meal): Boolean {

        val nutrition = meal.nutrition ?: return false
        val totalFat = nutrition.totalFatL ?: return false
        val carbs = nutrition.carbohydrates ?: return false

        return totalFat >= 20.0 && carbs <= 20.0
    }
}
