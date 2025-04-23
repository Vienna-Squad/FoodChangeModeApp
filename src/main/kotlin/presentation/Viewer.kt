package org.example.presentation

import org.example.logic.model.Meal

interface Viewer {
    fun showMealDetails(meal: Meal)
    fun showMealsDetails(meals: List<Meal>)
    fun showExceptionMessage(exception: Exception)
}