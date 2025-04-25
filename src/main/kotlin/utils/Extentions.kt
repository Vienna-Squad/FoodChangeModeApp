package org.example.utils

fun <T> List<T>.getRandomItem(): T? {
    if (isEmpty()) return null
    return getOrNull((0..(size - 1)).random())
}