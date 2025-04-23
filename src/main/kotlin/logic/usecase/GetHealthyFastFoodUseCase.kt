package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoHealthyFastFoodFoundException

class GetHealthyFastFoodUseCase(private val mealsRepository: MealsRepository) {

    operator fun invoke(): List<Meal> {
        val allMeals = mealsRepository.getAllMeals()
        if (allMeals.isEmpty()) {
            throw NoHealthyFastFoodFoundException("No meals available in repository")
        }

        val mealsWithNutrition = allMeals.filter { it.nutrition != null }
        if (mealsWithNutrition.isEmpty()) {
            throw NoHealthyFastFoodFoundException("No meals with nutrition data available")
        }

        val avgFat = allMeals.mapNotNull { it.nutrition?.totalFat }.average()
        val avgSaturatedFat = allMeals.mapNotNull { it.nutrition?.saturatedFat }.average()
        val avgCarbs = allMeals.mapNotNull { it.nutrition?.carbohydrates }.average()

        val healthyMeals = allMeals.filter { meal ->
            val nutrition = meal.nutrition ?: return@filter false

            val totalFat = nutrition.totalFat ?: return@filter false
            val saturatedFat = nutrition.saturatedFat ?: return@filter false
            val carbs = nutrition.carbohydrates ?: return@filter false
            val prepTime = meal.minutes ?: return@filter false

            val quickPrep = prepTime <= 15L
            val lowFat = totalFat < avgFat
            val lowSaturatedFat = saturatedFat < avgSaturatedFat
            val lowCarbs = carbs < avgCarbs

            quickPrep && lowFat && lowSaturatedFat && lowCarbs
        }

        if (healthyMeals.isEmpty()) {
            throw NoHealthyFastFoodFoundException()
        }

        return healthyMeals
    }
}