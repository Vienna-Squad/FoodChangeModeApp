package org.example.presentation.controllers

import org.example.logic.usecase.GetMealsOfCountryUseCase
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class MealByCountryUiController(
    private val getMealsOfCountryUseCase: GetMealsOfCountryUseCase = getKoin().get()
) : UiController {
    override fun execute() {
        print("Enter a  country to explore its meals: ")
        val input = readln()
        try {
            val meals = getMealsOfCountryUseCase(input)
            if (meals.isEmpty()) {
                println("No meals found for '$input'.")
            } else {
                println("Meals related to '$input':")
                meals.forEachIndexed { index, meal ->
                    println("${index + 1}. ${meal.name}")
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}