package org.example.presentation.controllers

import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class ItalianMealForGroupsUiController(
    private val getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        try {
            showMeals(getItalianGroupMealsUseCase())
        } catch (_: Exception) {
            println("No Italian meals for groups found")
        }
    }
}