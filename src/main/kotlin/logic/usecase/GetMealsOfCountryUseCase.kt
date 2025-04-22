package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.KMPSearcher


class GetMealsOfCountryUseCase(
    private val mealsRepository: MealsRepository,
    private val kmpSearcher: KMPSearcher = KMPSearcher()
) {
    /**
     * Explores meals related to a specified country using KMP string matching with partial match.
     * Searches in both tags and description, shuffles the results, and returns up to 20 meals.
     * @param countryInput The country name or adjective entered by the user (e.g., "Italy" or "Italian").
     * @throws NoMealFoundException if the country input is empty or not close to a real country.
     */
    operator fun invoke(countryInput: String): List<Meal> {
        if (countryInput.isBlank()) {
            throw NoMealFoundException("Country name cannot be empty.")
        }

        val normalizedCountry = countryInput.trim().lowercase()
        return mealsRepository.getAllMeals()
            .filter { meal -> hasMatchingCountry(meal, normalizedCountry) }
            .shuffled()
            .take(20)
    }

    private fun hasMatchingCountry(meal: Meal, countryInput: String): Boolean {
        val matchesInTags = meal.tags?.any { tag ->
            kmpSearcher.search(tag.lowercase(), countryInput)
        } ?: false

        val matchesInDescription = kmpSearcher.search(meal.description?.lowercase() ?: "", countryInput)
        return matchesInTags || matchesInDescription
    }
}
