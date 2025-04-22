package org.example.presentation.controllers

import org.example.logic.usecase.GetRandomPotatoMealsUseCase
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class PotatoesMealsUiController(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase = getKoin().get()
) : UiController {
    override fun execute() {
        getRandomPotatoMealsUseCase().forEach { meal ->
            println(" Name: ${meal.name}")
            println(" Ingredients: ${meal.ingredients ?: "No ingredients listed"}")
            println("------------------------------------------------------------")
        }
    }
}