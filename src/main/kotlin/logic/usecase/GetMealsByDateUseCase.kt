package org.example.logic.usecase

import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.NoMealFoundException
import java.text.SimpleDateFormat

class GetMealsByDateUseCase(private val mealsRepository: MealsRepository) {
    operator fun invoke(date: String) = mealsRepository.getAllMeals().filter { meal ->
        meal.submitted == dateFormat(date)
    }.let { meals ->
        if (meals.isEmpty()) throw NoMealFoundException("No meals were found for the given date")
        meals
    }

    private fun dateFormat(date: String) = try {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        formatter.parse(date)
    } catch (e: Exception) {
        throw IncorrectDateFormatException("Incorrect Date Format")
    }
}