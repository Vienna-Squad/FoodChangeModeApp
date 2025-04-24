package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetRandomPotatoMealsUseCase
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class PotatoesMealsUiController(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer()
) : UiController {
    override fun execute() {
        viewer.viewItems(getRandomPotatoMealsUseCase())
    }
}