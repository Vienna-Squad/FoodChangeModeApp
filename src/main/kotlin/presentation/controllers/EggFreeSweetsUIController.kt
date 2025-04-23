package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetEggFreeSweetsUseCase
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform

class EggFreeSweetsUIController(
    private val getEggFreeSweetsUseCase: GetEggFreeSweetsUseCase = KoinPlatform.getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        val seenMeals = mutableSetOf<Meal>()
        while (true) {
            val meal = getEggFreeSweetsUseCase(seenMeals)
            if (meal == null) {
                println("There Is no Egg-Free sweets")
                break
            }
            println("Meal: ${meal.name}")
            println(meal.description ?: "No description available.")
            println("1 - Like    |   2 - Dislike ")
            print("Your choice: ")
            when (readlnOrNull()) {
                "1" -> {
                    println("\n Full Details of ${meal.name}")
                    viewer.showMealDetails(meal)
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
        }
    }
}