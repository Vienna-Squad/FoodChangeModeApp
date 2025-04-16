package org.example.logic.model

data class Nutrition(
    val calories:Float?,
    val totalFat:Float?,
    val sugar:Float?,
    val sodium:Float?,
    val protein:Float?,
    val saturatedFat:Float?,
    val carbohydrates:Float?,
)

object NutritionEntityIndex{
    const val CALORIES = 0
    const val TOTAL_FAT = 1
    const val SUGAR = 2
    const val SODIUM = 3
    const val PROTEIN = 4
    const val SATURATED_FAT = 5
    const val CARBOHYDRATES = 6
}