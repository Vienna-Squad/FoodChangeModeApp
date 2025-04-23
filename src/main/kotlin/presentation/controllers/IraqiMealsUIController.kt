package org.example.presentation.controllers

import org.example.logic.usecase.GetIraqiMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.example.presentation.FoodViewer
import org.koin.mp.KoinPlatform.getKoin

class IraqiMealsUIController(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        try {
            viewer.showMealsDetails(getIraqiMealsUseCase())
        } catch (exception: NoMealFoundException) {
            viewer.showExceptionMessage(exception)
        }
    }
}