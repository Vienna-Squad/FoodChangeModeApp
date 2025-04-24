package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundException
import kotlin.random.Random

class GetEasyFoodSuggestionUseCase(private val mealsRepository: MealsRepository) {
    operator fun invoke() = mealsRepository.getAllMeals().filter{
        isEasyMeal(it)
    }.ifEmpty{
        throw NoMealFoundException("easy meals")
    }


     fun isEasyMeal(meal: Meal): Boolean {
        return  hasSixOrFewerStepsToPrepare(meal.numberOfSteps)
                &&isLessThanThirtyMinutesToMake(meal.minutes)
                &&isLessThanSixIngredients(meal.numberOfIngredients)
    }
     fun hasSixOrFewerStepsToPrepare(numberOfSteps: Int?): Boolean {
        return (numberOfSteps ?: 0) <= 6

    }
     fun isLessThanThirtyMinutesToMake(minutes: Long?): Boolean {
        return (minutes ?: 0) <= 30

    }
     fun isLessThanSixIngredients(numberOfIngredients: Int?): Boolean {
        return (numberOfIngredients ?: 0) <= 5
    }
}


