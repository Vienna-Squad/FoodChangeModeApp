package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class GetIraqiMealsUseCase(
    private val mealsRepository: MealsRepository
) {
    operator fun invoke() = mealsRepository.getAllMeals()
        .filter { meal -> hasIraqiTag(meal) || isDescriptionContainsIraqWord(meal) }

    private fun hasIraqiTag(meal: Meal) = meal.tags?.any { it.equals("iraqi", ignoreCase = true) } ?: false

    private fun isDescriptionContainsIraqWord(meal: Meal) =
        meal.description?.contains("Iraq", ignoreCase = true) ?: false
}