package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMatchingMealsFoundException
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

        require(desiredCalories > 0) { "Calories must be positive" }
        require(desiredProtein > 0) { "Protein must be positive" }

        val allMeals = mealsRepository.getAllMeals()
        if (allMeals.isEmpty()) {
            throw NoMatchingMealsFoundException("No meals available in repository")
        }

        val matchingMeals = allMeals.filter { meal ->
            val nutrition = meal.nutrition
            val calories = nutrition?.calories
            val protein = nutrition?.protein

            if (calories != null && protein != null) {
                abs(calories - desiredCalories) <= CALORIE_TOLERANCE &&
                        abs(protein - desiredProtein) <= PROTEIN_TOLERANCE
            } else {
                false
            }
        }

        if (matchingMeals.isEmpty()) {
            throw NoMatchingMealsFoundException()
        }

        return matchingMeals
    }
}