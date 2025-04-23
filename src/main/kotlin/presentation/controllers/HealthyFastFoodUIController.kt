package org.example.presentation.controllers

import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform.getKoin

class HealthyFastFoodUIController(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        try {
            val healthyFastFoodMeals = getHealthyFastFoodUseCase()
            if (healthyFastFoodMeals.isNotEmpty()) {
                println("================== Healthy Fast Food Suggestions ==================")
                viewer.showMealsDetails(healthyFastFoodMeals)
            } else {
                println("No healthy fast food options found based on the criteria.")
            }
        } catch (e: Exception) {
            println("Error fetching healthy fast food: ${e.message}")
        }
    }
}