package org.example.data

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class CsvMealsRepository(
    private val csvFileReader: CsvFileReader,
    private val mealsCsvParser: MealsCsvParser,
) : MealsRepository {
    private val allMeals: MutableList<Meal> = mutableListOf()

    init {
        parseCsvFileToListOfMeals()
    }

    override fun getAllMeals(): List<Meal> {
        if (allMeals.isEmpty()) parseCsvFileToListOfMeals()
        return allMeals
    }

    private fun parseCsvFileToListOfMeals() = csvFileReader.readLinesFromFile().drop(1).forEach {
        mealsCsvParser.parseOneLine(it).let { meal ->
            if (meal.description.isNullOrEmpty()) meal.description = null
            allMeals.add(meal)
        }
    }
}