package org.example.presentation.controllers

import org.example.logic.usecase.GetMealsOfCountryUseCase
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform.getKoin

class MealByCountryUiController(
    private val getMealsOfCountryUseCase: GetMealsOfCountryUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        print("Enter a  country to explore its meals: ")
        val input = readln()
        try {
            val meals = getMealsOfCountryUseCase(input)
            if (meals.isEmpty()) {
                println("No meals found for '$input'.")
            } else {
                viewer.showMealsDetails(meals)
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}