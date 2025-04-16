package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import kotlin.random.Random

class EasyFoodSuggestionUseCase(private val mealsRepository: MealsRepository) {
    fun getMeals():List<Meal> {
        return  mealsRepository.getAllMeals().filter(
            ::isPreparedInSixStepsOrFewer
        ).shuffled(Random).take(10)}
}

private fun isPreparedInSixStepsOrFewer(meal:Meal):Boolean{
    return  ((meal.numberOfSteps ?: 0) <= 6)
            && ((meal.minutes ?: 0) <= 30)
            && ((meal.numberOfIngredients ?: 0) < 5)
}
