package org.example.presentation.controllers

import org.example.logic.usecase.GetEasyFoodSuggestionUseCase
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform.getKoin

class EasyMealUIController(
    private val getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        val meals = getEasyFoodSuggestionUseCase()
        if (meals.isEmpty()) {
            println("No meals found")
        } else {
            println("Easy meals:")
            viewer.showMealsDetails(meals)
        }
    }
}