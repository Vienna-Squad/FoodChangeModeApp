package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class ItalianMealForGroupsUiController(
    private val getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer(),
) : UiController {
    override fun execute() {
            try {
                viewer.viewItems(getItalianGroupMealsUseCase())
            } catch (e: NoMealFoundException) {
                exceptionViewer.viewExceptionMessage(e)
            }
        }
}