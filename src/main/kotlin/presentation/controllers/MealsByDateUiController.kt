package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.interactor.Interactor
import org.example.utils.interactor.UserInteractor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class MealsByDateUiController(
    private val getMealsByDateUseCase: GetMealsByDateUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer(),
    private val interactor: Interactor=UserInteractor(),
    private val exceptionViewer: ExceptionViewer=FoodExceptionViewer()
) : UiController {
    override fun execute() {
        print("Enter date (dd/MM/yyyy): ")
        val inputDate = interactor.getInput()
        try {
            val meals = getMealsByDateUseCase(inputDate)
            println("Meals on $inputDate:")
            viewer.viewItems(meals)
            print("Enter meal ID to view details: ")
            val meal = readln().toLongOrNull()?.let { id ->
                meals.find { meal ->
                    meal.id == id
                }
            }
            println()
            println(meal ?: "No meal found with this ID.")
        } catch (e: IncorrectDateFormatException) {
            exceptionViewer.viewExceptionMessage(IncorrectDateFormatException(""))
        } catch (e: NoMealFoundException) {
           exceptionViewer.viewExceptionMessage(NoMealFoundException())
        }
    }

}