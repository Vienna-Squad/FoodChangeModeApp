package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import kotlin.random.Random

class GetRandomPotatoMealsUseCase(
    private val mealsRepository: MealsRepository
) {
    operator fun invoke(): List<Meal> {
        val potatoMeals = mealsRepository.getAllMeals()
            .filter(::hasPotatoes)

        if (potatoMeals.size <= 10) return potatoMeals

        val selectedIndices = mutableSetOf<Int>()
        while (selectedIndices.size < 10) {
            selectedIndices.add(Random.nextInt(potatoMeals.size))
        }

        return selectedIndices.map { potatoMeals[it] }
    }

    private fun hasPotatoes(meal: Meal): Boolean {
        return meal.ingredients?.any { it.contains("potato", ignoreCase = true) } ?: false
    }
}
