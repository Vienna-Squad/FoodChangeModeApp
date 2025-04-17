package org.example.logic.usecase

import org.example.logic.repository.MealsRepository

data class RankedMealResult(val rank: Int, val name: String?, val protein: Float?)

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
                val isSeafood = meal.tags?.any { tag ->
                    tag.trim().equals("seafood", ignoreCase = true)
                } ?: false
                val hasProtein = meal.nutrition?.protein != null
                isSeafood && hasProtein
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