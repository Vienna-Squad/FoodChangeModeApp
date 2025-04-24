package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealByNameUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.MealDetailsViewer
import org.example.presentation.*
import org.koin.mp.KoinPlatform.getKoin


class MealByNameUiController(
    private val getMealByNameUseCase: GetMealByNameUseCase = getKoin().get(),
    private val viewer: ItemDetailsViewer<Meal> = MealDetailsViewer(),
    private val interactor:Interactor=UserInteractor()
) : UiController {
    override fun execute() {
        print("Enter name to search for a meal: ")
        val inputName = interactor.getInput()
        try {
            val meal = getMealByNameUseCase(inputName)
            viewer.viewDetails(meal)
        } catch (e: NoMealFoundException) {
            viewer.showExceptionMessage(NoMealFoundException(inputName))
        }
    }
}