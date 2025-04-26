package org.example.utils.viewer

import org.example.presentation.model.IngredientGameDetails


class IngredientGameDetailsViewer: ItemDetailsViewer<IngredientGameDetails> {
    override fun viewDetails(item: IngredientGameDetails) {
        println("\nMeal : ${item.meal.name}")
        println("\nOptions : ${item.ingredients}\n")
        println("\t(1) first Option")
        println("\t(2) second Option")
        println("\t(3) third Option\n")
    }

}