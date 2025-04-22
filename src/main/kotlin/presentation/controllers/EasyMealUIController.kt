package org.example.presentation.controllers

import org.example.logic.usecase.GetEasyFoodSuggestionUseCase
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class EasyMealUIController(
    private val getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        val meals = getEasyFoodSuggestionUseCase()
        if (meals.isEmpty()) {
            println("No meals found")
        } else {
            println("Easy meals:")
            meals.forEach { meal ->
                showMealDetails(meal)
            }
        }
    }
}