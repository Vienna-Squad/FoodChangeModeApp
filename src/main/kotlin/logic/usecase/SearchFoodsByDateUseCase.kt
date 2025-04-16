package org.example.logic.usecase
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.MealsNotFoundForThisDateException
import java.text.SimpleDateFormat
import java.util.Date

class SearchFoodsByDateUseCase(

    private val mealsRepository: MealsRepository
) {

    fun dateFormat(date:String):Date{

        return try {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            formatter.parse(date)
        } catch (e: Exception) {
            throw IncorrectDateFormatException("Incorrect Date Format")
        }
    }

    operator fun invoke(date:String):List<Meal> {

        val mealsByDate=mealsRepository.getAllMeals().filter { meal ->
                meal.submitted == dateFormat(date)
            }

        if(mealsByDate.isEmpty()){
                throw MealsNotFoundForThisDateException("No meals were found for the given date")
            }

        else {
            return mealsByDate
        }


    }



}