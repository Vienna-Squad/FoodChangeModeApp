package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetKetoMealUseCase
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.MealDetailsViewer
import org.koin.mp.KoinPlatform.getKoin

class KetoMealsUiController(
    private val getKetoMealUseCase: GetKetoMealUseCase = getKoin().get(),
    private val viewer: ItemDetailsViewer<Meal> = MealDetailsViewer()
) : UiController {
    override fun execute() {
        val seenMeals = mutableSetOf<Meal>()
        while (true) {
            val meal = getKetoMealUseCase(seenMeals)
            if (meal == null) {
                println(" You've seen all keto meals")
                break
            }
            println("Meal: ${meal.name}")
            println(meal.description ?: "No description available.")
            println("1 - Like    |   2 - Dislike ")
            print("Your choice: ")
            when (readlnOrNull()) {
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
        }
    }
}