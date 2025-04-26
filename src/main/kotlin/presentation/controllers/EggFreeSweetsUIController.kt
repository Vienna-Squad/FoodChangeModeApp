package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetEggFreeSweetsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.interactor.Interactor
import org.example.utils.interactor.UserInteractor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.MealDetailsViewer
import org.koin.mp.KoinPlatform

class EggFreeSweetsUIController(
    private val getEggFreeSweetsUseCase: GetEggFreeSweetsUseCase = KoinPlatform.getKoin().get(),
    private val viewer: ItemDetailsViewer<Meal> = MealDetailsViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer(),
    private val interactor: Interactor = UserInteractor()
) : UiController {
    override fun execute() {
        val seenMeals = mutableSetOf<Meal>()

        while (true) {
            try {
                val meal = getEggFreeSweetsUseCase(seenMeals)

                println("Meal: ${meal.name}")
                println(meal.description ?: "No description available.")
                println("1 - Like    |   2 - Dislike ")
                print("Your choice: ")

                val input = interactor.getInput()
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
                        println("Exiting Egg-Free Sweets Suggestions")
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