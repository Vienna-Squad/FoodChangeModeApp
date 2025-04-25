package org.example.utils


class KMPSearcher {

    fun search(text: String, pattern: String): Boolean {
        if (pattern.isEmpty()) return false
        val textLength = text.length
        val patternLength = pattern.length
        if (patternLength > textLength) return false

        val longestPrefixSuffixArray = computeLongestPrefixSuffixArray(pattern)
        var textIndex = 0
        var patternIndex = 0

        while (textIndex < textLength) {
            if (pattern[patternIndex] == text[textIndex]) {
                textIndex++
                patternIndex++
            }
            if (patternIndex == patternLength) {
                return true
            } else if (textIndex < textLength && pattern[patternIndex] != text[textIndex]) {
                if (patternIndex != 0) {
                    patternIndex = longestPrefixSuffixArray[patternIndex - 1]
                } else {
                    textIndex++
                }
            }
        }
        return false
    }

    private fun computeLongestPrefixSuffixArray(pattern: String): IntArray {
        val longestPrefixSuffixArray = IntArray(pattern.length)
        var prefixLength = 0
        var patternIndex = 1

        longestPrefixSuffixArray[0] = 0

        while (patternIndex < pattern.length) {
            if (pattern[patternIndex] == pattern[prefixLength]) {
                prefixLength++
                longestPrefixSuffixArray[patternIndex] = prefixLength
                patternIndex++
            } else {
                if (prefixLength != 0) {
                    prefixLength = longestPrefixSuffixArray[prefixLength - 1]
                } else {
                    longestPrefixSuffixArray[patternIndex] = 0
                    patternIndex++
                }
            }
        }
        return longestPrefixSuffixArray
    }
}