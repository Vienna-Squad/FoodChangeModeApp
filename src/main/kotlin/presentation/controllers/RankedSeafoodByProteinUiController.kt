package org.example.presentation.controllers

import org.example.logic.usecase.GetRankedSeafoodByProteinUseCase
import org.example.logic.usecase.RankedMealResult
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class RankedSeafoodByProteinUiController(
    private val getRankedSeafoodByProteinUseCase: GetRankedSeafoodByProteinUseCase = getKoin().get()
) : UiController {
    override fun execute() {
        println("--- Seafood Meals Sorted by Protein (Highest First) ---")

        val results: List<RankedMealResult> = getRankedSeafoodByProteinUseCase()

        if (results.isEmpty()) {
            println("\u001B[33mNo seafood meals with protein information found.\u001B[0m")
        } else {
            // Green table header
            println("\u001B[32mRank | Meal Name                      | Protein (g)\u001B[0m")
            println("-----|--------------------------------|------------")
            results.forEach { rankedMeal ->
                // Format output for alignment
                val rankStr = rankedMeal.rank.toString().padEnd(4)
                val nameStr = (rankedMeal.name ?: "Unnamed Meal").take(30).padEnd(30) // Truncate long names
                val proteinStr = rankedMeal.protein?.toString() ?: "N/A"
                println("$rankStr | $nameStr | $proteinStr")
            }
        }
    }
}