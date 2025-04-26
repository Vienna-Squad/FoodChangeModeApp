package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoHealthyFastFoodFoundException

class GetHealthyFastFoodUseCase(private val mealsRepository: MealsRepository) {

    operator fun invoke(): List<Meal> {
        val allMeals = getAllMealsOrThrow()
        val mealsWithNutrition = getMealsWithNutritionOrThrow(allMeals)

        val avgFat = calculateAverage(mealsWithNutrition.mapNotNull { it.nutrition?.totalFat })
        val avgSaturatedFat = calculateAverage(mealsWithNutrition.mapNotNull { it.nutrition?.saturatedFat })
        val avgCarbs = calculateAverage(mealsWithNutrition.mapNotNull { it.nutrition?.carbohydrates })

        val healthyMeals = filterHealthyMeals(mealsWithNutrition, avgFat, avgSaturatedFat, avgCarbs)

        if (healthyMeals.isEmpty()) {
            throw NoHealthyFastFoodFoundException()
        }

        return healthyMeals
    }

    private fun getAllMealsOrThrow(): List<Meal> {
        val allMeals = mealsRepository.getAllMeals()
        if (allMeals.isEmpty()) {
            throw NoHealthyFastFoodFoundException("No meals available in repository")
        }
        return allMeals
    }

    private fun getMealsWithNutritionOrThrow(meals: List<Meal>): List<Meal> {
        val mealsWithNutrition = meals.filter { it.nutrition != null }
        if (mealsWithNutrition.isEmpty()) {
            throw NoHealthyFastFoodFoundException("No meals with nutrition data available")
        }
        return mealsWithNutrition
    }

    private fun calculateAverage(values: List<Float>): Float {
        return if (values.isEmpty()) 0f else values.average().toFloat()
    }

    private fun filterHealthyMeals(
        meals: List<Meal>,
        avgFat: Float,
        avgSaturatedFat: Float,
        avgCarbs: Float
    ): List<Meal> {
        return meals.filter { meal ->
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
    }
}