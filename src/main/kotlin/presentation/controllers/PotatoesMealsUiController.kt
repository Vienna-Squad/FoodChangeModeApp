package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetRandomPotatoMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class PotatoesMealsUiController(
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer()
) : UiController {
    override fun execute() {
        try {
            viewer.viewItems( getRandomPotatoMealsUseCase())
        } catch (exception: NoMealFoundException) {
            exceptionViewer.viewExceptionMessage(exception)
        }
    }
}