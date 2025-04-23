package org.example.presentation.controllers

import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.logic.usecase.exceptions.NoHealthyFastFoodFoundException
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
            println("================== Healthy Fast Food Suggestions ==================")
            val healthyFastFoodMeals = getHealthyFastFoodUseCase()
            viewer.showMealsDetails(healthyFastFoodMeals)
        } catch (e: NoHealthyFastFoodFoundException) {
            println("\u001B[33m${e.message}\u001B[0m")
        } catch (e: Exception) {
            println("\u001B[31mError fetching healthy fast food: ${e.message}\u001B[0m")
        }
    }
}