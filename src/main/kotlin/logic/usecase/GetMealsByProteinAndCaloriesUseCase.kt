package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import kotlin.math.abs

class GetMealsByProteinAndCaloriesUseCase(private val mealsRepository: MealsRepository) {

    private val CALORIE_TOLERANCE = 50.0f // +/- 50 calories
    private val PROTEIN_TOLERANCE = 5.0f  // +/- 5 grams of protein

    /**
     * Finds meals that match or approximate the desired calories and protein.
     *
     * @param desiredCalories The target calorie count.
     * @param desiredProtein The target protein amount.
     * @return A list of meals that fall within the allowed range.
     */
    operator fun invoke(desiredCalories: Float, desiredProtein: Float): List<Meal> {
        val allMeals = mealsRepository.getAllMeals()

        return allMeals.filter { meal ->

            val nutrition = meal.nutrition
            val calories = nutrition?.calories
            val protein = nutrition?.protein


            if (nutrition != null && calories != null && protein != null) {
                val calorieDiff = abs(calories - desiredCalories)
                val proteinDiff = abs(protein - desiredProtein)

                calorieDiff <= CALORIE_TOLERANCE && proteinDiff <= PROTEIN_TOLERANCE
            } else {
                false
            }
        }
    }
}