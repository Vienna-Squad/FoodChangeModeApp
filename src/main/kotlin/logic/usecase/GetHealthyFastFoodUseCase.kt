package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class GetHealthyFastFoodUseCase(private val mealsRepository: MealsRepository) {

    operator fun invoke(): List<Meal> {
        val allMeals = mealsRepository.getAllMeals()
        if (allMeals.isEmpty()) {
            return emptyList()
        }

        val avgFat = allMeals.mapNotNull { it.nutrition?.totalFat }.average()
        val avgSaturatedFat = allMeals.mapNotNull { it.nutrition?.saturatedFat }.average()
        val avgCarbs = allMeals.mapNotNull { it.nutrition?.carbohydrates }.average()

        return allMeals.filter { meal ->
            val quickPrep = (meal.minutes ?: 0L) <= 15L
            val lowFat = (meal.nutrition?.totalFat ?: Float.MAX_VALUE) < avgFat
            val lowSaturatedFat = (meal.nutrition?.saturatedFat ?: Float.MAX_VALUE) < avgSaturatedFat
            val lowCarbs = (meal.nutrition?.carbohydrates ?: Float.MAX_VALUE) < avgCarbs

            quickPrep && lowFat && lowSaturatedFat && lowCarbs
        }
    }
}