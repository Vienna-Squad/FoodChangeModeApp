package org.example.logic.usecase
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class SearchForMealByName(
    private val mealsRepository: MealsRepository

) {

    fun getMealByName(query:String): Meal{

        return mealsRepository.getAllMeals().find {meal->

            FuzzySearch.tokenSortRatio(meal.name?.lowercase(), query.lowercase())> 80

        }?:throw  NoMealFoundByNameException("No meal found matching the name: $query")

    }


}

class NoMealFoundByNameException(message:String):Exception(message)