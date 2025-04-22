package org.example.presentation.controllers

import org.example.logic.usecase.GetMealByNameUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class MealByNameUiController(
    private val getMealByNameUseCase: GetMealByNameUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        print("Enter name to search for a meal: ")
        val inputName = readln()
        try {
            val meal = getMealByNameUseCase(inputName)
            showMealDetails(meal)
        } catch (e: NoMealFoundException) {
            println(e.message)
        }
    }
}