package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.presentation.model.RankedMealResult
import org.example.logic.repository.MealsRepository

class GetRankedSeafoodByProteinUseCase(private val mealsRepository: MealsRepository) {

    /**
     * Gets a list of seafood meals sorted by protein (highest to lowest).
     *
     * @return A list of ranked meals with rank, name, and protein.
     */
    operator fun invoke(): List<Meal> {
        val allMeals = mealsRepository.getAllMeals()

        return allMeals

            .filter { meal ->
                var isSeafood = false
                meal.tags?.forEach { tag ->
                    if (tag.trim().contains("seafood", ignoreCase = true)) {
                        isSeafood = true
                    }
                }
                isSeafood
            }
            .sortedByDescending { meal ->
                meal.nutrition?.protein ?: 0.0f
            }
    }
}