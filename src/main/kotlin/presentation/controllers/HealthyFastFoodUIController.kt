package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.logic.usecase.exceptions.NoHealthyFastFoodFoundException
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class HealthyFastFoodUIController(
    private val getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer(),
    private val viewerException: ExceptionViewer = FoodExceptionViewer()
) : UiController {
    override fun execute() {
        try {
            println("================== Healthy Fast Food Suggestions ==================")
            val healthyFastFoodMeals = getHealthyFastFoodUseCase()
            viewer.viewItems(healthyFastFoodMeals)
        } catch (e: NoHealthyFastFoodFoundException) {
            viewerException.viewExceptionMessage(e)
        } catch (e: Exception) {
            viewerException.viewExceptionMessage(e)
        }
    }
}