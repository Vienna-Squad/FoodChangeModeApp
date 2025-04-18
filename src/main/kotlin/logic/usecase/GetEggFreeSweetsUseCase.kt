package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class GetEggFreeSweetsUseCase(
    private val mealsRepository: MealsRepository
) {
    operator fun invoke(seenMeals: MutableSet<Meal>) = mealsRepository.getAllMeals()
        .filter { isEggFreeSweet(it) && it !in seenMeals }
        .randomOrNull()

    private fun isEggFreeSweet(meal: Meal): Boolean {

        return (meal.ingredients?.none {
            it.contains("Egg", ignoreCase = true)
        } == true)
                && meal.ingredients.any {
            it.contains("Sugar", ignoreCase = true)
        }
                && (meal.tags?.any {
            it.contains("Dessert", ignoreCase = true)
        } == true)

    }
}

