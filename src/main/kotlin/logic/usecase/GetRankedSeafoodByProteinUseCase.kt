package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoSeafoodFoundException
import org.example.presentation.model.RankedMealResult

class GetRankedSeafoodByProteinUseCase(private val mealsRepository: MealsRepository) {

    /**
     * Gets a list of seafood meals sorted by protein (highest to lowest).
     *
     * @return A list of ranked meals with rank, name, and protein.
     */
    operator fun invoke(): List<RankedMealResult> {
        val allMeals = mealsRepository.getAllMeals()
        if (allMeals.isEmpty()) {
            throw NoSeafoodFoundException("No meals available in repository")
        }

        val seafoodMeals = allMeals
            .filter { isSeafoodMeal(it) }
            .filter { it.nutrition?.protein != null }
            .sortedByDescending { it.nutrition?.protein ?: 0f }


        if (seafoodMeals.isEmpty()) {
            throw NoSeafoodFoundException()
        }


        return seafoodMeals.mapIndexed { index, meal ->
            RankedMealResult(
                rank = index + 1,
                name = meal.name,
                protein = meal.nutrition?.protein
            )
        }
    }

    private fun isSeafoodMeal(meal: Meal): Boolean {
        return meal.tags?.any { tag ->
            tag.contains("seafood", ignoreCase = true) ||
                    tag.contains("fish", ignoreCase = true) ||
                    tag.contains("shrimp", ignoreCase = true) ||
                    tag.contains("salmon", ignoreCase = true) ||
                    tag.contains("tuna", ignoreCase = true)
        } ?: false
    }
}