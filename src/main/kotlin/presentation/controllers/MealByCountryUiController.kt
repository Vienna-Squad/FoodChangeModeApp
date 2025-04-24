package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealsOfCountryUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.interactor.Interactor
import org.example.utils.interactor.UserInteractor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class MealByCountryUiController(
    private val getMealsOfCountryUseCase: GetMealsOfCountryUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer(),
    private val interactor: Interactor = UserInteractor()
) : UiController {
    override fun execute() {
        print("Enter a country to explore its meals: ")
        val input =interactor.getInput()
            try {
                val meals = getMealsOfCountryUseCase(input)
                viewer.viewItems(meals)
            } catch (e: NoMealFoundException) {
                exceptionViewer.viewExceptionMessage(e)
            }
    }
}

