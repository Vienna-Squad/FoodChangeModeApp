package org.example.utils.fuzzysearch

interface TextMatcher {
    fun isMatch(text1: String?, text2: String?, threshold: Int): Boolean
}