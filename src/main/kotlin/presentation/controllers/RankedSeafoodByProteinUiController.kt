package org.example.presentation.controllers


import org.example.logic.usecase.GetRankedSeafoodByProteinUseCase
import org.example.logic.usecase.exceptions.NoSeafoodFoundException
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.koin.mp.KoinPlatform.getKoin

class RankedSeafoodByProteinUiController(
    private val getRankedSeafoodByProteinUseCase: GetRankedSeafoodByProteinUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        try {
            println("--- Seafood Meals Sorted by Protein (Highest First) ---")
            val results = getRankedSeafoodByProteinUseCase()

            if (results.isEmpty()) {
                println("\u001B[33mNo seafood meals with protein information found.\u001B[0m")
            } else {
                println("\u001B[32mRank | Meal Name                      | Protein (g)\u001B[0m")
                println("-----|--------------------------------|------------")
                results.forEach { rankedMeal ->
                    val rankStr = rankedMeal.rank.toString().padEnd(4)
                    val nameStr = (rankedMeal.name ?: "Unnamed Meal").take(30).padEnd(30)
                    val proteinStr = rankedMeal.protein?.toString() ?: "N/A"
                    println("$rankStr | $nameStr | $proteinStr")
                }
            }
        } catch (e: NoSeafoodFoundException) {
            viewer.showExceptionMessage(e)
        } catch (e: Exception) {
            viewer.showExceptionMessage(e)
        }
    }
}