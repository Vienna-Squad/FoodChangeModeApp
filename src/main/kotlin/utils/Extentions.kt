package org.example.utils

fun <T> List<T>.getRandomItem() = getOrNull((0..(size - 1)).random())