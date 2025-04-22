package org.example.presentation

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition

abstract class MealDetailsViewer {
    fun showMealDetails(meal: Meal) {
        println("Name : ${meal.name}")
        println("Description : ${meal.description ?: "no description"}")
        println("Prepare Minutes : ${meal.minutes}")
        println("Ingredients : ${meal.ingredients}")
        println("Steps : ${meal.steps}")
        showNutrition(meal.nutrition!!)
    }

    private fun showNutrition(nutrition: Nutrition) {
        val map = mutableMapOf(
            "calories" to nutrition.calories,
            "sodium" to nutrition.sodium,
            "sugar" to nutrition.sugar,
            "protein" to nutrition.protein,
            "totalFat" to nutrition.totalFat,
            "carbohydrates" to nutrition.carbohydrates,
            "saturatedFat" to nutrition.saturatedFat,
        )
        println("Nutrition : $map")
    }

    fun showMeals(meals: List<Meal>) {
        meals.forEach { meal ->
            showMealDetails(meal)
            println("------------------------------------------------------------")
        }
    }
}