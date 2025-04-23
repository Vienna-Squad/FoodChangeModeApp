package org.example.presentation

import org.example.logic.model.Meal
import org.example.logic.model.RankedMealResult

interface Viewer {
    fun showMealDetails(meal: Meal)
    fun showMealsDetails(meals: List<Meal>)
    fun showExceptionMessage(exception: Exception)
    fun showRankedMealsByProtein(meals: List<RankedMealResult>)
}