package org.example.presentation.controllers

import org.example.logic.usecase.GetHighCalorieMealUseCase
import org.example.logic.usecase.SuggestHighCalorieMealUseCase
import org.example.logic.usecase.exceptions.InvalidInputNumberOfHighCalorieMeal
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.Viewer
import org.example.utils.showNameAndDescription
import org.koin.mp.KoinPlatform.getKoin

class HighCalorieMealUIController(
    private val getHighCalorieMeal: GetHighCalorieMealUseCase = getKoin().get(),
    private val viewer: Viewer = FoodViewer()
) : UiController {
    override fun execute() {
        do {
            try {
                println("The Suggestion High Calorie Meal ")
                getHighCalorieMeal.getRandomHighCalorieMeal().showNameAndDescription()
                println("\t(1) Like (show more details about meal)")
                println("\t(2) Dislike (show another meal)")
                println("\t(3) Exit ")
                print("Input Your Choose Number : ")
                val inputUser = readln().toIntOrNull()

                when (inputUser) {

                    1 -> { viewer.showMealDetails(getHighCalorieMeal.getRandomHighCalorieMeal()); break }

                    2 -> println("Get Another Suggestion Meal \n")

                    3 -> break

                    else -> throw InvalidInputNumberOfHighCalorieMeal("$inputUser is not in valid range (0..3) , please try again\n\n")
                }
            } catch (emptyException: NoMealFoundException) {
                print("Error : ${emptyException.message}")
            } catch (invalidInputException: InvalidInputNumberOfHighCalorieMeal) {
                print("Error : ${invalidInputException.message}")
            } catch (error: Exception) {
                println("Error : ${error.message}")
            }

        } while (true)
    }
}