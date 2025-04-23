package org.example.logic.usecase


import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.EmptyRandomMealException


class GetHighCalorieMealUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getRandomHighCalorieMeal(): Meal {
        return mealsRepository.getAllMeals()
            .filter { meal -> filterNameAndDescriptionAndCalories(meal) && filterMealOnlyContainMoreThan700Calories(meal) }
            .take(20)
            .randomOrNull()
            ?: throw EmptyRandomMealException("the meal contain more than 700 calorie is not found in data")
    }


    private fun filterNameAndDescriptionAndCalories(meal: Meal): Boolean {
        return meal.name != null && meal.description != null && meal.nutrition?.calories != null
    }

    private fun filterMealOnlyContainMoreThan700Calories(meal: Meal): Boolean {
        return meal.nutrition?.calories!! > 700f
    }
}




