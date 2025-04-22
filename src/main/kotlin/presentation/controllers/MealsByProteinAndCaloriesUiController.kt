package org.example.presentation.controllers

import org.example.logic.usecase.GetMealsByProteinAndCaloriesUseCase
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class MealsByProteinAndCaloriesUiController(
    private val getMealsByProteinAndCaloriesUseCase: GetMealsByProteinAndCaloriesUseCase = getKoin().get()
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
            results.forEach { meal ->
                // Display meal name and relevant nutrition
                val caloriesStr = meal.nutrition?.calories?.toString() ?: "N/A"
                val proteinStr = meal.nutrition?.protein?.toString() ?: "N/A"
                println("- ${meal.name ?: "Unnamed Meal"} (Calories: $caloriesStr, Protein: ${proteinStr}g)")
            }
        }
    }
}