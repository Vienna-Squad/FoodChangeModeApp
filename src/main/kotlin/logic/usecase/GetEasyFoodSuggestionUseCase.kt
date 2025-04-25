package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundException
import kotlin.random.Random

class GetEasyFoodSuggestionUseCase(private val mealsRepository: MealsRepository) {
    operator fun invoke():List<Meal> {
        val randomEasyMeals= mutableSetOf <Meal>()
        val allEasyMeals=mealsRepository.getAllMeals().filter{
            isEasyMeal(it)
        }.ifEmpty{
            throw NoMealFoundException("easy meals")
        }
        if (allEasyMeals.size<=10)return allEasyMeals
        while (randomEasyMeals.size<10){
            val randomIndex = Random.nextInt(0, allEasyMeals.size)
            val randomMeal = allEasyMeals[randomIndex]
                randomEasyMeals.add(randomMeal)
            }
        return randomEasyMeals.toList()
    }
    private fun isEasyMeal(meal: Meal): Boolean {
        return  hasSixOrFewerStepsToPrepare(meal.numberOfSteps)
                &&isLessThanThirtyMinutesToMake(meal.minutes)
                &&hasLessThanSixIngredients(meal.numberOfIngredients)
    }
     private fun hasSixOrFewerStepsToPrepare(numberOfSteps: Int?): Boolean {
        return (numberOfSteps ?: 0) <= 6

    }
    private fun isLessThanThirtyMinutesToMake(minutes: Long?): Boolean {
        return (minutes ?: 0) <= 30

    }
    private fun hasLessThanSixIngredients(numberOfIngredients: Int?): Boolean {
        return (numberOfIngredients ?: 0) <= 5
    }
}


