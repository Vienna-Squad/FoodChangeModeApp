package org.example.utils.fuzzysearch

import me.xdrop.fuzzywuzzy.FuzzySearch

class FuzzyTextMatcher(
    private val defaultThreshold: Int = 85
) : TextMatcher {
    override fun isMatch(text1: String?, text2: String?, threshold: Int): Boolean {
        if (text1.isNullOrBlank() || text2.isNullOrBlank()) {
            return false
        }
        val normalizedText1 = normalize(text1)
        val normalizedText2 = normalize(text2)
        return FuzzySearch.ratio(normalizedText1, normalizedText2) >= threshold
    }

    private fun normalize(input: String): String = input.lowercase().replace("-", " ").trim()
}