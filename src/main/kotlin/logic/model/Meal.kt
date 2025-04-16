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
    var description:String?,
    val ingredients:List<String>?,
    val numberOfIngredients : Int?,
)
object MealEntityIndex{
    const val NAME = 0
    const val ID = 1
    const val MINUTES = 2
    const val CONTRIBUTOR_ID = 3
    const val SUBMITTED = 4
    const val TAGS = 5
    const val NUTRITION = 6
    const val NUMBER_OF_STEPS = 7
    const val STEPS = 8
    const val DESCRIPTION = 9
    const val INGREDIENTS = 10
    const val NUMBER_OF_INGREDIENTS = 11
}
