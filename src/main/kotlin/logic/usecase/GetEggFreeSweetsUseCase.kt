package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class GetEggFreeSweetsUseCase(
    private val mealsRepository: MealsRepository
) {
    operator fun invoke(seenMeals: MutableSet<Meal>) = mealsRepository.getAllMeals()
        .filter { isEggFreeSweet(it) == true && it !in seenMeals }
        .randomOrNull()

     private fun isEggFreeSweet(meal: Meal): Boolean? {
         return meal.ingredients?.any{ it.contains("Egg", ignoreCase = true) }
     }
     }


