package org.example.logic.usecase

import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import kotlin.random.Random

class GetEasyFoodSuggestionUseCase(private val mealsRepository: MealsRepository) {
    operator fun invoke():List<Meal> {
        return  mealsRepository.getAllMeals().filter(
            ::isPreparedInSixStepsOrFewer
        ).shuffled(Random).take(10)}

    private fun isPreparedInSixStepsOrFewer(meal:Meal):Boolean{
        return  ((meal.numberOfSteps ?: 0) <= 6)
                && ((meal.minutes ?: 0) <= 30)
                && ((meal.numberOfIngredients ?: 0) < 5)
    }
}


