package org.example.logic.model

import java.util.Date

data class Meal(
    val name:String?,
    val id: Long?,
    val minutes: Long?,
    val contributorId: Long?,
    val submitted: Date,
    val tags:List<String>?,
    val nutrition: Nutrition?,
    val numberOfSteps : Int?,
    val steps:List<String>?,
    val description:String?,
    val ingredients:List<String>?,
    val numberOfIngredients : Int?,
)
