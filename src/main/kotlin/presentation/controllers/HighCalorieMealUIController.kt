package org.example.presentation.controllers

import org.example.logic.usecase.EmptyRandomMealException
import org.example.logic.usecase.InvalidInputNumberOfHighCalorieMeal
import org.example.logic.usecase.SuggestHighCalorieMealUseCase
import org.example.presentation.MealDetailsViewer
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class HighCalorieMealUIController(
    private val suggestHighCalorieMealUseCase: SuggestHighCalorieMealUseCase = getKoin().get()
) : MealDetailsViewer(), UiController {
    override fun execute() {
        do {
            try {
                println("The Suggestion High Calorie Meal ")
                val nameAndDescription = suggestHighCalorieMealUseCase.suggestNameAndDescriptionOfHighCalorieMeal()
                suggestHighCalorieMealUseCase.checkMealIsFound(nameAndDescription)
                println("name : ${nameAndDescription.first} , Description : ${nameAndDescription.second}")
                println("\nIf you want more details about meal press (1) ")
                println("if you want another meal press (2) ")
                println("if you want To Exit (3) ")
                print("Input Your Choose Number : ")
                val inputUser = readln().toIntOrNull()

                when (inputUser) {

                    1 -> {
                        showMealDetails(
                            suggestHighCalorieMealUseCase.getSuggestionHighCalorieMealDetails(nameAndDescription.first!!)
                        )
                        break
                    }

                    2 -> println("Another Suggestion Meal \n")

                    3 -> break

                    else -> throw InvalidInputNumberOfHighCalorieMeal("$inputUser is not in valid range (0..3) , please try again\n\n")
                }
            } catch (emptyException: EmptyRandomMealException) {
                print("Error : ${emptyException.message}")
            } catch (invalidInputException: InvalidInputNumberOfHighCalorieMeal) {
                print("Error : ${invalidInputException.message}")
            } catch (error: Exception) {
                println("Error : ${error.message}")
            }

        } while (true)
    }
}