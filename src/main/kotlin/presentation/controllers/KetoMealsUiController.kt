package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetKetoMealUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.interactor.Interactor
import org.example.utils.interactor.UserInteractor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.MealDetailsViewer
import org.koin.mp.KoinPlatform.getKoin

class KetoMealsUiController(
    private val getKetoMealUseCase: GetKetoMealUseCase = getKoin().get(),
    private val viewer: ItemDetailsViewer<Meal> = MealDetailsViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer(),
    private val interactor: Interactor = UserInteractor()
) : UiController {

    override fun execute() {
        val seenMeals = mutableSetOf<Meal>()


        while (true) {
            try {
                val meal = getKetoMealUseCase(seenMeals)
                val input = interactor.getInput()

                println("Meal: ${meal.name}")
                println(meal.description ?: "No description available.")
                println("1 - Like    |   2 - Dislike ")
                print("Your choice: ")

                when (input) {
                    "1" -> {
                        println("\n Full Details of ${meal.name}")
                        viewer.viewDetails(meal)
                        break
                    }
                    "2" -> {
                        seenMeals.add(meal)
                        println("\n try another one\n")
                    }
                    else -> {
                        println("Exiting Keto Meal Suggestions")
                        break
                    }
                }
            } catch (e: NoMealFoundException) {
                exceptionViewer.viewExceptionMessage(e)
                break
            }
        }
    }
}
