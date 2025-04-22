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
        val map = mutableMapOf<String, Float?>()
        map.put("calories", nutrition.calories)
        map.put("sodium", nutrition.sodium)
        map.put("sugar", nutrition.sugar)
        map.put("protein", nutrition.protein)
        map.put("totalFat", nutrition.totalFat)
        map.put("carbohydrates", nutrition.carbohydrates)
        map.put("saturatedFat", nutrition.saturatedFat)
        println("Nutrition : $map")
    }
}