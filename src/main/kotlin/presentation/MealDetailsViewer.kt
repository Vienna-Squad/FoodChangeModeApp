package org.example.presentation

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition

abstract class MealDetailsViewer {
    fun showMealDetails(meal: Meal) {
        println("name : ${meal.name}")
        println("ingredients : ${meal.description}")
        println("minutes : ${meal.minutes}")
        println("ingredients : ${meal.ingredients}")
        println("steps : ${meal.steps}")
        showNutrition(meal.nutrition!!)
    }

    private fun showNutrition(nutrition: Nutrition) {
        println("calories : ${nutrition.calories}")
        println("sodium : ${nutrition.sodium}")
        println("sugar  : ${nutrition.sugar}")
        println("protein : ${nutrition.protein}")
        println("totalFat : ${nutrition.totalFat}")
        println("carbohydrates : ${nutrition.carbohydrates}")
        println("saturatedFat : ${nutrition.saturatedFat}")
    }
}