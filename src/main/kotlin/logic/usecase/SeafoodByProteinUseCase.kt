package org.example.logic.usecase

import org.example.logic.repository.MealsRepository

// This data class helps us represent each ranked seafood meal with relevant info
data class RankedMealResult(val rank: Int, val name: String?, val protein: Float?)


class SeafoodByProteinUseCase(private val mealsRepository: MealsRepository) {

    /**
     * Gets a list of seafood meals sorted by protein (highest to lowest).
     *
     * @return A list of ranked meals with rank, name, and protein.
     */
    fun getRankedSeafoodMeals(): List<RankedMealResult> {
        val allMeals = mealsRepository.getAllMeals()

        return allMeals
            .filter { meal ->
                // Check if the meal is tagged as seafood (case-insensitive)
                val isSeafood = meal.tags?.any { it.equals("seafood", ignoreCase = true) } == true
                // Check if protein information exists
                val hasProtein = meal.nutrition?.protein != null

                // Keep only seafood meals with protein info
                isSeafood && hasProtein
            }
            .sortedByDescending { meal ->
                // Sort descending by protein
                // Using 0.0f as a safe default in case of null (although we filtered)
                meal.nutrition?.protein ?: 0.0f
            }
            .mapIndexed { index, meal ->
                // Assign a rank and build the result object
                RankedMealResult(
                    rank = index + 1, // Rank starts from 1
                    name = meal.name,
                    protein = meal.nutrition?.protein
                )
            }
    }
}