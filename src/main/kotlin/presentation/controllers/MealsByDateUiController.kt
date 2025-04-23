package org.example.presentation.controllers

import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.*
import org.koin.mp.KoinPlatform.getKoin

class MealsByDateUiController(
    private val getMealsByDateUseCase: GetMealsByDateUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer(),
    private val interactor: Interactor=UserInteractor()
) : UiController {
    override fun execute() {
        print("Enter date (dd/MM/yyyy): ")
        val inputDate = interactor.getInput()
        try {
            val meals = getMealsByDateUseCase(inputDate)
            println("Meals on $inputDate:")
            viewer.showMealsDetails(meals)
            print("Enter meal ID to view details: ")
            val meal = readln().toLongOrNull()?.let { id ->
                meals.find { meal ->
                    meal.id == id
                }
            }
            println()
            println(meal ?: "No meal found with this ID.")
        } catch (e: IncorrectDateFormatException) {
            viewer.showExceptionMessage(IncorrectDateFormatException(""))
        } catch (e: NoMealFoundException) {
            viewer.showExceptionMessage(NoMealFoundException())
        }
    }

}