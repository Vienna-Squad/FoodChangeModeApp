package org.example.presentation.controllers

import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class HealthyFastFoodUIController(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        try {
            val healthyFastFoodMeals = getHealthyFastFoodUseCase()
            if (healthyFastFoodMeals.isNotEmpty()) {
                println("================== Healthy Fast Food Suggestions ==================")
                showMeals(healthyFastFoodMeals)
            } else {
                println("No healthy fast food options found based on the criteria.")
            }
        } catch (e: Exception) {
            println("Error fetching healthy fast food: ${e.message}")
        }
    }
}