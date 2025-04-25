package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealByNameUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.MealDetailsViewer
import org.example.presentation.*
import org.example.utils.interactor.Interactor
import org.example.utils.interactor.UserInteractor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.koin.mp.KoinPlatform.getKoin


class MealByNameUiController(
    private val getMealByNameUseCase: GetMealByNameUseCase = getKoin().get(),
    private val viewer: ItemDetailsViewer<Meal> = MealDetailsViewer(),
    private val exceptionViewer: ExceptionViewer=FoodExceptionViewer(),
    private val interactor: Interactor = UserInteractor()
) : UiController {
    override fun execute() {
        print("Enter name to search for a meal: ")
        val inputName = interactor.getInput()
        try {
            val meal = getMealByNameUseCase(inputName)
            viewer.viewDetails(meal)
        } catch (e: NoMealFoundException) {
            exceptionViewer.viewExceptionMessage(NoMealFoundException(inputName))
        }
    }
}