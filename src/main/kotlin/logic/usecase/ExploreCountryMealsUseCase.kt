package org.example.logic.usecase
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.utils.fuzzysearch.FuzzyCountryMatcher

class NotACountryException(message: String) : Exception(message)
class ExploreCountryMealsUseCase(
    private val mealsRepository: MealsRepository,
    private val fuzzyCountryMatcher: FuzzyCountryMatcher = FuzzyCountryMatcher()
) {
    /**
     * Retrieves a list of meals associated with the specified country.
     * The method validates the country input, checks if it matches a valid country name using fuzzy matching,
     * and then filters meals based on their tags to find those related to the input country.
     * The resulting list is shuffled and limited to a maximum of 20 meals.
     *
     * @param countryInput The name of the country to search for (e.g.,"Egypt" ,"Italy", "Mexican").
     * @return A list of up to 20 meals associated with the specified country, randomly shuffled.
     * @throws NotACountryException If the country input is blank or does not match any valid country name.
     */
    fun exploreMealsByCountry(countryInput: String): List<Meal> {
        if (countryInput.isBlank()) {
            throw NotACountryException("Country name cannot be empty.")
        }
        if (!fuzzyCountryMatcher.isValidCountry(countryInput)) {
            throw NotACountryException("'$countryInput' is not a valid country name.")
        }
        return mealsRepository.getAllMeals()
            .filter { meal -> hasMatchingCountryTag(meal, countryInput) }
            .shuffled()
            .take(20)
    }

    private fun hasMatchingCountryTag(meal: Meal, countryInput: String): Boolean {
        return meal.tags?.any { tag ->
            fuzzyCountryMatcher.doesTagMatchCountry(tag, countryInput)
        } ?: false
    }
}

