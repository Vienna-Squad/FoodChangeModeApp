package org.example.utils

class KMPSearcher {
    /**
     * Searches for a pattern in a text using the Knuth-Morris-Pratt (KMP) algorithm.
     * @param text The text to search in.
     * @param pattern The pattern to search for.
     * @return True if the pattern is found in the text, false otherwise.
     */
    fun search(text: String, pattern: String): Boolean {
        if (pattern.isEmpty()) return false
        val n = text.length
        val m = pattern.length
        if (m > n) return false

        val lps = computeLPSArray(pattern)
        var i = 0
        var j = 0

        while (i < n) {
            if (pattern[j] == text[i]) {
                i++
                j++
            }
            if (j == m) {
                return true
            } else if (i < n && pattern[j] != text[i]) {
                if (j != 0) {
                    j = lps[j - 1]
                } else {
                    i++
                }
            }
        }
        return false
    }


    private fun computeLPSArray(pattern: String): IntArray {
        val lps = IntArray(pattern.length)
        var length = 0
        var i = 1

        lps[0] = 0

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++
                lps[i] = length
                i++
            } else {
                if (length != 0) {
                    length = lps[length - 1]
                } else {
                    lps[i] = 0
                    i++
                }
            }
        }
        return lps
    }
}