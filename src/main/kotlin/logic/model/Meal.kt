package org.example.logic.model

import java.time.LocalDate

data class Meal(
    val name:String,
    val id: Long,
    val minutes: String,
    val contributorId: Long,
    val submitted: LocalDate,
    val tags:List<String>,
    val nutrition: List<Nutrition>,
    val noOfSteps: Any,
    val steps:List<String>,
    val description:String,
    val ingredients:List<String>,
    val noOfIngredients: Any,
)
