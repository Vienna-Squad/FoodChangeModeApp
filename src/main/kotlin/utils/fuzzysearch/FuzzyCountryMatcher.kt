package org.example.utils.fuzzysearch

import java.util.*

class FuzzyCountryMatcher(
    private val textMatcher: TextMatcher = FuzzyTextMatcher(),
    private val threshold: Int = 85
) {
    private val countryNames: List<String> = Locale.getISOCountries().map { Locale("", it).displayCountry.lowercase() }

    private fun areInputsValid(vararg inputs: String): Boolean = inputs.all { it.isNotBlank() }
    private fun matchesCountry(text: String): Boolean =
        countryNames.any { country -> textMatcher.isMatch(text, country, threshold) }

    fun isValidCountry(countryInput: String): Boolean = areInputsValid(countryInput) && matchesCountry(countryInput)


    fun doesTagMatchCountry(tag: String, countryInput: String): Boolean =
        areInputsValid(tag, countryInput) &&
                countryNames.any { country ->
                    textMatcher.isMatch(tag, country, threshold) &&
                            textMatcher.isMatch(countryInput, country, threshold)
                }
}

