package org.example.presentation.controllers

import org.example.logic.usecase.GetIraqiMealsUseCase
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class IraqiMealsUIController(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        getIraqiMealsUseCase().let { meals ->
            if (meals.isEmpty()) {
                println("IraqiMealsNotFound")
            } else {
                showMeals(meals)
            }
        }
    }
}