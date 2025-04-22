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
    operator fun invoke(): List<Meal> {
        val matchingMeals = mealsRepository.getAllMeals()
            .filter(::hasItalianAndGroupTags)
        if (matchingMeals.isEmpty()) {
            throw NoItalianGroupMealsException("No meals found with tags 'italian' and 'for-large-groups' in the Csv file.")
        }

        return matchingMeals
    }

    private fun hasItalianAndGroupTags(meal: Meal): Boolean {
        val tags = meal.tags?.map { it.lowercase() } ?: return false
        return tags.any { it.contains("italian") } && tags.any { it.contains("for-large-groups") }
    }
}