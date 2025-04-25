package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class HealthyFastFoodUIController(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer()
) : UiController {
    override fun execute() {
        try {
            val healthyFastFoodMeals = getHealthyFastFoodUseCase()
            if (healthyFastFoodMeals.isNotEmpty()) {
                println("================== Healthy Fast Food Suggestions ==================")
                viewer.viewItems(healthyFastFoodMeals)
            } else {
                println("No healthy fast food options found based on the criteria.")
            }
        } catch (e: Exception) {
            println("Error fetching healthy fast food: ${e.message}")
        }
    }
}