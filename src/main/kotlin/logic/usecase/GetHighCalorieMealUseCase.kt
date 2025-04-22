package org.example.logic.usecase


import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NullRandomMealException


class GetHighCalorieMealUseCase(
    private val mealsRepository: MealsRepository
) {

    fun getNameAndDescription(): Pair<String?, String?>{
//        return mealsRepository.getAllMeals()
           return emptyList<Meal>()
            .filter(::filterNameAndDescriptionAndCalories)
            .filter(::filterMealOnlyContainMoreThan700Calories)
            .take(30)
            .map { it.name to it.description }
            .randomOrNull()?:throw NullRandomMealException("The meal with more than 700 calorie in meal list")
    }

    private val mealName = getNameAndDescription()?.first

    fun getMealDetailsByName(name: String = mealName?:""): Meal {
        return mealsRepository.getAllMeals()
            .firstOrNull{ meal -> meal.name == name }
            ?: throw NullRandomMealException("the name : $name is not found in meal list")
    }

    private fun filterNameAndDescriptionAndCalories(meal: Meal): Boolean {
        return meal.name !== null && meal.description != null && meal.nutrition?.calories!=null
    }

    private fun filterMealOnlyContainMoreThan700Calories(meal: Meal): Boolean {
        return meal.nutrition?.calories!! > 700f
    }

}

