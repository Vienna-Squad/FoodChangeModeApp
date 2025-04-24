package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetHighCalorieMealUseCase
import org.example.logic.usecase.exceptions.FoodChangeMoodException
import org.example.logic.usecase.exceptions.InvalidInputNumberOfHighCalorieMeal
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.viewer.AnotherSuggestionMealViewer
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.HighCalorieMealViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.MealDetailsViewer
import org.koin.mp.KoinPlatform.getKoin

class HighCalorieMealUIController(
    private val suggestHighCalorieMealUseCase: GetHighCalorieMealUseCase = getKoin().get(),
    private val showMealDetailsViewer: ItemDetailsViewer<Meal> = MealDetailsViewer(),
    private val highCalorieMealViewer: ItemDetailsViewer<Meal> = HighCalorieMealViewer(),
    private val anotherSuggestionMealViewer: ItemDetailsViewer<String> = AnotherSuggestionMealViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer()
) : UiController {
    override fun execute() {
        do {
            try {

                val meal = suggestHighCalorieMealUseCase.getRandomHighCalorieMeal()
                highCalorieMealViewer.viewDetails(meal)

                print(INPUT_USER_MESSAGE)
                val inputUser = readln().toIntOrNull()

                when (inputUser) {

                    1 -> {
                        showMealDetailsViewer.viewDetails(suggestHighCalorieMealUseCase.getRandomHighCalorieMeal())
                        break
                    }

                    2 -> anotherSuggestionMealViewer.viewDetails(ANOTHER_SUGGESTION_MEAL_MESSAGE)

                    3 -> break

                    else -> throw InvalidInputNumberOfHighCalorieMeal(INVALID_INPUT_NUMBER_EXCEPTION)
                }
            } catch (emptyException: NoMealFoundException) {
                exceptionViewer.viewExceptionMessage(emptyException)
            } catch (invalidInputException: InvalidInputNumberOfHighCalorieMeal) {
                exceptionViewer.viewExceptionMessage(invalidInputException)
            } catch (generalException: Exception) {
                exceptionViewer.viewExceptionMessage(generalException)
            }

        } while (true)
    }

    companion object {
        const val ANOTHER_SUGGESTION_MEAL_MESSAGE = "Show Another Suggestion Meal"
        const val INPUT_USER_MESSAGE = "Input Your Choose Number : "
        const val INVALID_INPUT_NUMBER_EXCEPTION = " Input User Number is not in valid range (0..3) , please try again\n\n"
    }
}