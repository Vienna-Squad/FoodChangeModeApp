package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealsOfCountryUseCase
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class MealByCountryUiController(
    private val getMealsOfCountryUseCase: GetMealsOfCountryUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer()
) : UiController {
    override fun execute() {
        print("Enter a  country to explore its meals: ")
        val input = readln()
        try {
            val meals = getMealsOfCountryUseCase(input)
            if (meals.isEmpty()) {
                println("No meals found for '$input'.")
            } else {
                viewer.viewItems(meals)
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}