package org.example.data

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

 class CsvMealsRepository(
    private val csvFileReader: CsvFileReader,
    private val mealsCsvParser:MealsCsvParser,
) :MealsRepository

{
    override fun getAllMeals():List<Meal> {
        val mealsList= mutableListOf<Meal>()
        csvFileReader.readLinesFromFile().drop(1).dropLast(1).forEach {
           val meal= mealsCsvParser.parseOneLine(it)
            mealsList.add(meal)
        }
        return mealsList
    }

}