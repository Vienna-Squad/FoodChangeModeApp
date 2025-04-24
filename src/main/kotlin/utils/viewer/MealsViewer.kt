package org.example.utils.viewer

import org.example.logic.model.Meal

class MealsViewer(
    private val itemDetailsViewer: ItemDetailsViewer<Meal> = MealDetailsViewer()
) : ItemsViewer<Meal> {
    override fun viewItems(items: List<Meal>) {
        items.forEach { meal ->
            itemDetailsViewer.viewDetails(meal)
            println("------------------------------------------------------------")
        }
    }
}