package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.KMPSearcher
import org.example.utils.getRandomItem


class GetMealsOfCountryUseCase(
    private val mealsRepository: MealsRepository,
    private val kmpSearcher: KMPSearcher = KMPSearcher()
) {

    operator fun invoke(countryInput: String): List<Meal> {
        if (countryInput.isBlank()) {
            throw NoMealFoundException("Country name cannot be empty.")
        }
        val normalizedCountry = countryInput.trim().lowercase()
        val filteredMeals = mealsRepository.getAllMeals()
            .filter { meal -> hasMatchingCountry(meal, normalizedCountry) }
        return selectRandomMeals(filteredMeals)
    }


    private fun hasMatchingCountry(meal: Meal, countryInput: String): Boolean {
        val matchesInTags = meal.tags?.any { tag ->
            kmpSearcher.search(tag.lowercase(), countryInput)
        } ?: false

        val matchesInDescription = meal.description?.let { desc ->
            kmpSearcher.search(desc.lowercase(), countryInput)
        } ?: false

        return matchesInTags || matchesInDescription
    }


    private fun selectRandomMeals(filteredMeals: List<Meal>): List<Meal> {
        if (filteredMeals.size <= 20 && filteredMeals.isNotEmpty()) {
            return filteredMeals
        }
        val meals = mutableSetOf<Meal>()
        while (meals.size < 20) {
            val randomMeals = filteredMeals.getRandomItem() ?: throw NoMealFoundException()
            meals.add(randomMeals)
        }
        return meals.toList()
    }
}
