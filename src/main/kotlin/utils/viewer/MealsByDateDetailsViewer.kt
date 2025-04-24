package org.example.utils.viewer

import org.example.logic.model.Meal

class MealsByDateDetailsViewer:ItemsViewer<Meal> {

    override fun viewItems(items: List<Meal>) {
        items.forEach { meal ->
            println("ID : ${meal.id}, Name : ${meal.name}")
        }
    }
}