package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoItalianGroupMealsException

class GetItalianGroupMealsUseCase(private val mealsRepository: MealsRepository) {

    /**
     * Use case to suggest  Italian meals suitable for large groups.
     * Filters meals by checking if they have both 'italian' and 'for-large-groups' tags.
     * @throws NoItalianGroupMealsException if no suitable meals are found.
     */
    operator fun invoke() = mealsRepository.getAllMeals()
        .filter(::isItalianGroupMeal)
        .let { meals ->
            if (meals.isEmpty()) throw NoItalianGroupMealsException("No meals found with tags 'italian' and 'for-large-groups' in the Csv file.")
            meals
        }

    private fun isItalianGroupMeal(meal: Meal) = meal.tags?.let { tags ->
        tags.contains("italian") && tags.contains("for-large-groups")
    } ?: false
}
