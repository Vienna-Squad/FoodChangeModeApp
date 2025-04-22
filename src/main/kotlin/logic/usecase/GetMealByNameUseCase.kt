package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundByNameException
import org.example.utils.KMPSearcher

class GetMealByNameUseCase(
    private val mealsRepository: MealsRepository,
    private val kmpSearcher: KMPSearcher = KMPSearcher()
) {
    operator fun invoke(query: String): Meal {
        val normalizedQuery = query.trim().lowercase()
        return mealsRepository.getAllMeals().find { meal ->
            val name = meal.name?.lowercase().orEmpty()
            kmpSearcher.search(name, normalizedQuery)
        } ?: throw NoMealFoundByNameException("No meal found matching the name: $query")
    }
}

