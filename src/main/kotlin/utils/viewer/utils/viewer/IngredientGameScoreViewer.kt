package org.example.utils.viewer.utils.viewer

import org.example.utils.viewer.ItemDetailsViewer

class IngredientGameScoreViewer: ItemDetailsViewer<Int> {
    override fun viewDetails(score: Int) {
        println("Final Score : $score")
    }

}