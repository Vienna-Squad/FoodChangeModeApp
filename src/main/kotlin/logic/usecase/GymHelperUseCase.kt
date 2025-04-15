package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import kotlin.math.abs

// This use case helps users find meals that closely match their fitness goals
class GymHelperUseCase(private val mealsRepository: MealsRepository) {

    private val CALORIE_TOLERANCE = 50.0f // +/- 50 calories
    private val PROTEIN_TOLERANCE = 5.0f  // +/- 5 grams of protein

    /**
     * Finds meals that match or approximate the desired calories and protein.
     *
     * @param desiredCalories The target calorie count.
     * @param desiredProtein The target protein amount.
     * @return A list of meals that fall within the allowed range.
     */
    fun findMeals(desiredCalories: Float, desiredProtein: Float): List<Meal> {
        val allMeals = mealsRepository.getAllMeals()

        return allMeals.filter { meal ->
            // Ensure the meal has nutritional info, calories, and protein
            val nutrition = meal.nutrition
            val calories = nutrition?.calories
            val protein = nutrition?.protein

            // Only consider meals with complete nutritional data
            if (nutrition != null && calories != null && protein != null) {
                // Check if the values fall within the allowed tolerance range
                val calorieDiff = abs(calories - desiredCalories)
                val proteinDiff = abs(protein - desiredProtein)

                // Keep the meal if it's within the allowed range
                calorieDiff <= CALORIE_TOLERANCE && proteinDiff <= PROTEIN_TOLERANCE
            } else {
                // Skip meals that don't have complete data
                false
            }
        }
         // this if I want to return the result sorted
        // .sortedBy { abs((it.nutrition?.calories ?: 0f) - desiredCalories) }
    }
}