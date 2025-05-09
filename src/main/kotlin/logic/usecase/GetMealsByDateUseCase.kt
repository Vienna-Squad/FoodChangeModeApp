package org.example.logic.usecase

import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.NoMealFoundException
import java.text.SimpleDateFormat
import java.util.*

class GetMealsByDateUseCase(private val mealsRepository: MealsRepository) {
    operator fun invoke(date: Date) = mealsRepository.getAllMeals().filter { meal ->
        meal.submitted == date
    }.let { meals ->
        if (meals.isEmpty()) throw NoMealFoundException("No meals were found for the given date")
        meals
    }

    
}