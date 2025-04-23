package org.example.presentation.controllers

import org.example.logic.model.RankedMealResult
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
            viewer.showRankedMealsByProtein(results)
        } catch (e: NoSeafoodFoundException) {
            println("\u001B[33m${e.message}\u001B[0m")
        } catch (e: Exception) {
            viewer.showExceptionMessage(e)
        }
    }
}