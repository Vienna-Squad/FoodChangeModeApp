package org.example.data
import org.example.logic.model.Meal
import org.example.logic.model.MealEntityIndex
import org.example.logic.model.Nutrition
import org.example.logic.model.NutritionEntityIndex
import java.text.ParseException

import java.text.SimpleDateFormat
import java.util.*


class MealsCsvParser {

    fun parseOneLine(line: String): Meal {
        val meal = smartCsvParser(line)
         try {
           return Meal(
                name = meal[MealEntityIndex.NAME],
                id = meal[MealEntityIndex.ID].toLong(),
                minutes = meal[MealEntityIndex.MINUTES].toLong(),
                contributorId = meal[MealEntityIndex.CONTRIBUTOR_ID].toLong(),
                submitted = constructDate(meal[MealEntityIndex.SUBMITTED]),
                tags = consTructListFromString(meal[MealEntityIndex.TAGS]),
                nutrition = constructNutritionObject(meal[MealEntityIndex.NUTRITION]),
                numberOfSteps = meal[MealEntityIndex.NUMBER_OF_STEPS].toInt(),
                steps = consTructListFromString(meal[MealEntityIndex.STEPS]),
                description = meal[MealEntityIndex.DESCRIPTION],
                ingredients = consTructListFromString(meal[MealEntityIndex.INGREDIENTS]),
                numberOfIngredients = meal[MealEntityIndex.NUMBER_OF_INGREDIENTS].toInt()
            )
        }
        catch (numberFormatException:NumberFormatException){
            throw NumberFormatException("Invalid number format in line: $line")
        }
        catch (parseException: ParseException){
            throw ParseException("Invalid date format in line: $line", parseException.errorOffset)
        }
    }

    private fun constructDate(dateString: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        try {
            return formatter.parse(dateString)
        }catch (parseException:ParseException){
            throw parseException
        }

    }

    private fun constructNutritionObject(nutritionField: String): Nutrition {
        val nutrition = try {
            consTructListFromString(nutritionField).map { it.toFloat() }
        }catch (message:NumberFormatException){
           throw message
        }
        return Nutrition(
            calories = nutrition[NutritionEntityIndex.CALORIES],
            totalFat = nutrition[NutritionEntityIndex.TOTAL_FAT],
            sugar = nutrition[NutritionEntityIndex.SUGAR],
            sodium = nutrition[NutritionEntityIndex.SODIUM],
            protein = nutrition[NutritionEntityIndex.PROTEIN],
            saturatedFat = nutrition[NutritionEntityIndex.SATURATED_FAT],
            carbohydrates = nutrition[NutritionEntityIndex.CARBOHYDRATES]
        )
    }

    private fun consTructListFromString(string: String): List<String> {
        return string
            .removePrefix("[")
            .removeSuffix("]")
            .splitToSequence(",")
            .map { it.trim().removeSurrounding(",")
                it.trim().removeSurrounding("\'")
            }
            .toList()
    }

    private fun smartCsvParser(line: String): List<String> {
        val mealEntities = mutableListOf<String>()
        val currentEntity = StringBuilder()
        var insideQuotes = false
        var i = 0

        while (i < line.length) {
            when (val char = line[i]) {
                '"' -> {
                    if (line[i + 1] == '"') {
                        currentEntity.append('"')
                        i++
                    } else {
                        insideQuotes = !insideQuotes
                    }
                }
                ',' -> {
                    if (!insideQuotes) {
                        mealEntities.add(currentEntity.toString().trim())
                        currentEntity.clear()
                    } else {
                        currentEntity.append(char)
                    }
                }
                else -> currentEntity.append(char)
            }
            i++
        }
        if (currentEntity.isNotEmpty()) {
            mealEntities.add(currentEntity.toString().trim())
        }
        return mealEntities
    }
}

