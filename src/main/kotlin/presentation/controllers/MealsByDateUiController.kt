package org.example.presentation.controllers

import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class MealsByDateUiController(
    private val getMealsByDateUseCase: GetMealsByDateUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        print("Enter date (dd/MM/yyyy): ")
        val inputDate = readln()
        try {
            val meals = getMealsByDateUseCase(inputDate)
            println("Meals on $inputDate:")
            showMeals(meals)
            print("Enter meal ID to view details: ")
            val meal = readln().toLongOrNull()?.let { id ->
                meals.find { meal ->
                    meal.id == id
                }
            }
            println()
            println(meal ?: "No meal found with this ID.")
        } catch (e: IncorrectDateFormatException) {
            println("${e.message} Please use dd/MM/yyyy format.")
        } catch (e: NoMealFoundException) {
            println(e.message)
        }
    }

}