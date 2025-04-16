package org.example.logic.repository

import org.example.logic.model.Meal

interface MealsRepository {

    fun getAllMeals():List<Meal>
}