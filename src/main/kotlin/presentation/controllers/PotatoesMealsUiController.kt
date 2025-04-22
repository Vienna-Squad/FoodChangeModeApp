package org.example.presentation.controllers

import org.example.logic.usecase.GetRandomPotatoMealsUseCase
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class PotatoesMealsUiController(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        showMeals(getRandomPotatoMealsUseCase())
    }
}