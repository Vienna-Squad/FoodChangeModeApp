package org.example.presentation

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import kotlin.collections.forEach

class FoodViewer : Viewer {
    override fun showMealDetails(meal: Meal) {
        println("Name : ${meal.name}")
        println("Description : ${meal.description ?: "no description"}")
        println("Prepare Minutes : ${meal.minutes}")
        println("Ingredients : ${meal.ingredients}")
        println("Steps : ${meal.steps}")
        println("Nutrition : ${nutritionToString(meal.nutrition!!)}")
    }

    private fun nutritionToString(nutrition: Nutrition): String {
        return mutableMapOf(
            "calories" to nutrition.calories,
            "sodium" to nutrition.sodium,
            "sugar" to nutrition.sugar,
            "protein" to nutrition.protein,
            "totalFat" to nutrition.totalFat,
            "carbohydrates" to nutrition.carbohydrates,
            "saturatedFat" to nutrition.saturatedFat,
        ).toString()
    }

    override fun showMealsDetails(meals: List<Meal>) {
        meals.forEach { meal ->
            showMealDetails(meal)
            println("------------------------------------------------------------")
        }
    }

    override fun showExceptionMessage(exception: Exception) {
        println("\"\\u001B[31m${exception.message}\\u001B[0m\"")
    }
}