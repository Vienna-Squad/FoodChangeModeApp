package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class ItalianMealForGroupsUiController(
    private val getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer()
) : UiController {
    override fun execute() {
        try {
            viewer.viewItems(getItalianGroupMealsUseCase())
        } catch (_: Exception) {
            println("No Italian meals for groups found")
        }
    }
}