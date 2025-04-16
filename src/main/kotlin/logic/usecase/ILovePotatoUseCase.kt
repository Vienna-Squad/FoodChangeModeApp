package org.example.logic.usecase
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class ILovePotatoUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getMeals(): List<Meal> {
        return mealsRepository.getAllMeals()
            .filter(::hasPotatoes)
            .shuffled()
            .take(10)
    }

    private fun hasPotatoes(meal: Meal): Boolean {
        return meal.ingredients
            ?.any { it.contains("potato", ignoreCase = true) } ?: false
    }
}