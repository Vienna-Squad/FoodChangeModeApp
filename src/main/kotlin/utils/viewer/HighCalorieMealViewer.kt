package org.example.utils.viewer

import org.example.logic.model.Meal

class HighCalorieMealViewer : ItemDetailsViewer<Meal> {
    override fun viewDetails(item: Meal) {
        println("The Suggestion High Calorie Meal ")
        println("Name : ${item.name}\tDescription : ${item.description}")
        println("\t(1) Like ")
        println("\t(2) Dislike ")
        println("\t(3) Exit ")
    }

}