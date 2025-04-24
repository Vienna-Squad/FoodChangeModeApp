package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.viewer.ItemsViewer
import org.example.utils.viewer.MealsViewer
import org.example.presentation.*
import org.koin.mp.KoinPlatform.getKoin
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.util.Date

class MealsByDateUiController(
    private val getMealsByDateUseCase: GetMealsByDateUseCase = getKoin().get(),
    private val viewer: ItemsViewer<Meal> = MealsViewer()
) : UiController {
    override fun execute() {
        print("Enter date (dd/MM/yyyy): ")
        val inputDate = interactor.getInput()

        try {

            val date: Date = dateFormat(inputDate)
            val mealsByDate = getMealsByDateUseCase(date)

            println("Meals on $inputDate:")
            showIdAndNameMeals(mealsByDate)

            viewer.viewItems(meals)
            print("Enter meal ID to view details: ")
            val mealId=interactor.getInput()
            getMealById(mealId,mealsByDate)

        } catch (e: IncorrectDateFormatException) {
            viewer.showExceptionMessage(IncorrectDateFormatException("${e.message} Please use dd/MM/yyyy format."))
        } catch (e: NoMealFoundException) {
            viewer.showExceptionMessage(e)
        }
    }

    fun showIdAndNameMeals(mealsByDate: List<Meal>) {
        mealsByDate.forEach { meal ->
            println("ID : ${meal.id}, Name : ${meal.name}")
        }
    }

    fun getMealById(mealId:String,mealsByDate:List<Meal>){

        try {
            mealId.toLongOrNull()?.let { id ->
                mealsByDate.find { meal ->
                    meal.id == id
                }
                println()
            }
        }catch (e:NoMealFoundException) {
            viewer.showExceptionMessage(e)
        }
    }

    private fun dateFormat(date:String): Date {

        return try {
            val formatter= DateTimeFormatterBuilder().appendPattern("d/M/yyyy").toFormatter()
            val localDate= LocalDate.parse(date,formatter)
            Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        }catch (e:Exception){
            throw IncorrectDateFormatException("Incorrect date format")
        }


    }

}