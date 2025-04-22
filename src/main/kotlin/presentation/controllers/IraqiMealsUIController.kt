package org.example.presentation.controllers

import org.example.logic.usecase.GetIraqiMealsUseCase
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class IraqiMealsUIController(
    private val getIraqiMealsUseCase: GetIraqiMealsUseCase = getKoin().get()
): UiController {
    override fun execute() {
        getIraqiMealsUseCase().let { meals ->
            if (meals.isEmpty()) {
                println("IraqiMealsNotFound")
            } else {
                meals.forEach { println(it) }
            }
        }
    }
}