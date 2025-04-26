package org.example.presentation.controllers

import org.example.logic.usecase.GetRankedSeafoodByProteinUseCase
import org.example.logic.usecase.exceptions.NoSeafoodFoundException
import org.example.presentation.model.RankedMealResult
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.koin.mp.KoinPlatform.getKoin

class RankedSeafoodByProteinUiController(
    private val getRankedSeafoodByProteinUseCase: GetRankedSeafoodByProteinUseCase = getKoin().get(),
    private val viewer: ExceptionViewer = FoodExceptionViewer()
) : UiController {
    override fun execute() {
        try {
            println("--- Seafood Meals Sorted by Protein (Highest First) ---")
            val results = getRankedSeafoodByProteinUseCase()

            if (results.isEmpty()) {
                displayEmptyResults()
            } else {
                displayResults(results)
            }
        } catch (e: NoSeafoodFoundException) {
            viewer.viewExceptionMessage(e)
        } catch (e: Exception) {
            viewer.viewExceptionMessage(e)
        }

    }


    private fun displayEmptyResults() {
        println("\u001B[33mNo seafood meals with protein information found.\u001B[0m")
    }

    private fun displayResults(results: List<RankedMealResult>) {
        println("\u001B[32mRank | Meal Name                      | Protein (g)\u001B[0m")
        println("-----|--------------------------------|------------")
        results.forEach { rankedMeal ->
            printMeal(rankedMeal)
        }
    }

    private fun printMeal(rankedMeal: RankedMealResult) {
        val rankStr = rankedMeal.rank.toString().padEnd(4)
        val nameStr = (rankedMeal.name ?: "Unnamed Meal").take(30).padEnd(30)
        val proteinStr = rankedMeal.protein?.toString() ?: "N/A"
        println("$rankStr | $nameStr | $proteinStr")
    }
}