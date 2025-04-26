package org.example.presentation.model

import org.example.logic.model.Meal

data class IngredientGameDetails(
    val meal: Meal,
    val ingredients: List<String?>,
)
