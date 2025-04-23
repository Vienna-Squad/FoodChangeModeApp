package org.example.presentation.controllers

import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.IncorrectDateFormatException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.util.Date

class MealsByDateUiController(
    private val getMealsByDateUseCase: GetMealsByDateUseCase = getKoin().get()
) : UiController {
    override fun execute() {
        print("Enter date (dd/MM/yyyy): ")
        val inputDate = readln()

        try {

            val date: Date = dateFormat(inputDate)
            val mealsByDate = getMealsByDateUseCase(date)


            println("Meals on $inputDate:")
            mealsByDate.forEach { meal ->
                println("ID : ${meal.id}, Name : ${meal.name}")
            }
            print("Enter meal ID to view details: ")
            val mealId = readln().toLongOrNull()?.let { id ->
                mealsByDate.find { meal ->
                    meal.id == id
                }
            }
            println()
            println(mealId ?: "No meal found with this ID.")
        } catch (e: IncorrectDateFormatException) {
            println("${e.message} Please use dd/MM/yyyy format.")
        } catch (e: NoMealFoundException) {
            println(e.message)
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