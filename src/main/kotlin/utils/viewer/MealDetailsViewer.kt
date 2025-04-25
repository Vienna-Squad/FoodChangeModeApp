package org.example.utils.viewer

import org.example.logic.model.Meal
import org.example.logic.model.Nutrition

class MealDetailsViewer : ItemDetailsViewer<Meal> {
    override fun viewDetails(item: Meal) {
        println("Name : ${item.name}")
        println("Description : ${item.description ?: "no description"}")
        println("Prepare Minutes : ${item.minutes}")
        println("Ingredients : ${item.ingredients}")
        println("Steps : ${item.steps}")
        println("Nutrition : ${nutritionToString(item.nutrition!!)}")
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
}