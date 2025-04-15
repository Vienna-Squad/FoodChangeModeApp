package org.example.logic.usecase
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class NoItalianGroupMealsException(message: String) : Exception(message)
class ItalianGroupMealUseCase(
    private val mealsRepository: MealsRepository
) {

    /**
     * Use case to suggest  Italian meals suitable for large groups.
     * Filters meals by checking if they have both 'italian' and 'for-large-groups' tags.
     * @throws NoItalianGroupMealsException if no suitable meals are found.
     */
    fun getMeals(): List<Meal> {

        val filteredMeals = mealsRepository.getAllMeals()
            .filter(::isItalianGroupMeal)

        if (filteredMeals.isEmpty()) {
            throw NoItalianGroupMealsException("No meals found with tags 'italian' and 'for-large-groups' in the Csv file.")
        }

        return filteredMeals
    }


    private fun isItalianGroupMeal(meal: Meal): Boolean {
        return meal.tags?.let { tags ->
            tags.contains("italian") && tags.contains("for-large-groups")
        } ?: false
    }
}
