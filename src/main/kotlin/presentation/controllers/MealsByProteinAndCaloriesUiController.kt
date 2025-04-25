package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealsByProteinAndCaloriesUseCase
import org.example.logic.usecase.exceptions.NoMatchingMealsFoundException
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.koin.mp.KoinPlatform.getKoin

class MealsByProteinAndCaloriesUiController(
    private val getMealsByProteinAndCaloriesUseCase: GetMealsByProteinAndCaloriesUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer()
) : UiController {
    override fun execute() {
        try {
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

            println("\u001B[32mFound ${results.size} matching meals:\u001B[0m")
            viewer.viewItems(results)

        } catch (e: IllegalArgumentException) {
            println("\u001B[31mError: ${e.message}\u001B[0m")
        } catch (e: NoMatchingMealsFoundException) {
            println("\u001B[33m${e.message}\u001B[0m")
        } catch (e: Exception) {
            println("\u001B[31mUnexpected error: ${e.message}\u001B[0m")
        }
    }
}