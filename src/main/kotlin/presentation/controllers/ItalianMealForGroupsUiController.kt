package org.example.presentation.controllers

import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class ItalianMealForGroupsUiController(
    private val getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase = getKoin().get()
) : UiController {
    override fun execute() {
        try {
            val meals = getItalianGroupMealsUseCase()
            meals.forEach { meal ->
                println(meal.name)
            }
        } catch (e: Exception) {
            println("No Italian meals for groups found")
        }
    }
}