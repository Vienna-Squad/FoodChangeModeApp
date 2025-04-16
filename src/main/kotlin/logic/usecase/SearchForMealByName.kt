package org.example.logic.usecase
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository

class SearchForMealByName(
    private val mealsRepository: MealsRepository

) {

    operator fun invoke(query:String): Meal{

        val  threshold=80

        return mealsRepository.getAllMeals().find {meal->

            FuzzySearch.tokenSortRatio(meal.name?.lowercase(), query.lowercase())> threshold

        }?:throw  NoMealFoundByNameException("No meal found matching the name: $query")

    }


}

class NoMealFoundByNameException(message:String):Exception(message)