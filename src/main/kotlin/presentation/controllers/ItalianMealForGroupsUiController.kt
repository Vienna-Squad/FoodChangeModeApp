package org.example.presentation.controllers

import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform.getKoin

class ItalianMealForGroupsUiController(
    private val getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        try {
            viewer.showMealsDetails(getItalianGroupMealsUseCase())
        } catch (_: Exception) {
            println("No Italian meals for groups found")
        }
    }
}