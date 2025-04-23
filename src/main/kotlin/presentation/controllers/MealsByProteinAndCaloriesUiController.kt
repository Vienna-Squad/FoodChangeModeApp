package org.example.presentation.controllers

import org.example.logic.usecase.GetMealsByProteinAndCaloriesUseCase
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform.getKoin

class MealsByProteinAndCaloriesUiController(
    private val getMealsByProteinAndCaloriesUseCase: GetMealsByProteinAndCaloriesUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        println("--- Find Meals by Calories & Protein ---")
        print("Enter desired calories (e.g., 500): ")
        val caloriesInput = readln().toFloatOrNull()
        print("Enter desired protein in grams (e.g., 30): ")
        val proteinInput = readln().toFloatOrNull()

        if (caloriesInput == null || proteinInput == null) {
            println("\u001B[31mInvalid input. Please enter numbers only for calories and protein.\u001B[0m")
            return
        }

        val results = getMealsByProteinAndCaloriesUseCase(caloriesInput, proteinInput)

        if (results.isEmpty()) {
            println("\u001B[33mNo meals found matching your criteria within the tolerance.\u001B[0m")
        } else {
            println("\u001B[32mFound ${results.size} matching meals:\u001B[0m")
            viewer.showMealsDetails(results)
        }
    }
}