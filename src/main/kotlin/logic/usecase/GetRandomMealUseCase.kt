package org.example.logic.usecase

import org.example.logic.repository.MealsRepository

class GetRandomMealUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getMeal() = mealsRepository.getAllMeals().random()
}