package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class GetRandomPotatoMealsUseCase(
    private val mealsRepository: MealsRepository
) {
    operator fun invoke() = mealsRepository.getAllMeals()
        .filter(::hasPotatoes)
        .shuffled()
        .take(10)

    private fun hasPotatoes(meal: Meal) = meal.ingredients
        ?.any { it.contains("potato", ignoreCase = true) } ?: false
}