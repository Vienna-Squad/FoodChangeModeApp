package org.example.logic.usecase

import org.example.logic.model.RankedMealResult
import org.example.logic.repository.MealsRepository

class GetRankedSeafoodByProteinUseCase(private val mealsRepository: MealsRepository) {

    /**
     * Gets a list of seafood meals sorted by protein (highest to lowest).
     *
     * @return A list of ranked meals with rank, name, and protein.
     */
    operator fun invoke(): List<RankedMealResult> {
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
            .mapIndexed { index, meal ->
                RankedMealResult(
                    rank = index + 1, // Rank starts from 1
                    name = meal.name,
                    protein = meal.nutrition?.protein
                )
            }
    }
}