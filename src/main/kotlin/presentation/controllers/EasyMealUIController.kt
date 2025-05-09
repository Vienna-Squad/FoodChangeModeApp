package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetEasyFoodSuggestionUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class EasyMealUIController(
    private val getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer()
) : UiController {
    override fun execute() {
        val meals = getEasyFoodSuggestionUseCase()
        if (meals.isEmpty()) {
            throw NoMealFoundException("easy meals")
        } else {
            println("Easy meals:")
            viewer.viewItems(meals)
        }
    }
}