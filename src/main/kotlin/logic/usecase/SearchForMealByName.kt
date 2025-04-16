package org.example.logic.usecase

import me.xdrop.fuzzywuzzy.FuzzySearch
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundByNameException

class SearchForMealByName(private val mealsRepository: MealsRepository) {
    operator fun invoke(query: String) = mealsRepository.getAllMeals().find { meal ->
        FuzzySearch.tokenSortRatio(meal.name?.lowercase(), query.lowercase()) > 80
    } ?: throw NoMealFoundByNameException("No meal found matching the name: $query")
}

