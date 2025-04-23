package org.example.presentation.controllers

import org.example.logic.usecase.GetRandomPotatoMealsUseCase
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform.getKoin

class PotatoesMealsUiController(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        viewer.showMealsDetails(getRandomPotatoMealsUseCase())
    }
}