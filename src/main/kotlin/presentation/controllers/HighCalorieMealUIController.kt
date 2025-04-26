package org.example.presentation.controllers

import org.example.logic.model.Meal
import org.example.logic.usecase.GetHighCalorieMealUseCase
import org.example.logic.usecase.exceptions.EmptyRandomMealException
import org.example.logic.usecase.exceptions.InvalidInputNumberOfHighCalorieMeal
import org.example.utils.interactor.HighCalorieMealInteractor
import org.example.utils.interactor.UserHighCalorieMealInteractor
import org.example.utils.viewer.*
import org.koin.mp.KoinPlatform.getKoin

class HighCalorieMealUIController(
    private val suggestHighCalorieMealUseCase: GetHighCalorieMealUseCase = getKoin().get(),
    private val showMealDetailsViewer: ItemDetailsViewer<Meal> = MealDetailsViewer(),
    private val highCalorieMealViewer: ItemDetailsViewer<Meal> = HighCalorieMealViewer(),
    private val anotherSuggestionMealViewer: ItemDetailsViewer<String> = MessageViewer(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer(),
    private val highCalorieMealInteractor: HighCalorieMealInteractor = UserHighCalorieMealInteractor()
) : UiController {
    override fun execute() {
        try {
            var counter = 0
            do {
                val meal = suggestHighCalorieMealUseCase.getRandomHighCalorieMeal()
                highCalorieMealViewer.viewDetails(meal)

                print(INPUT_USER_MESSAGE)
                val inputUser = highCalorieMealInteractor.getInput()

                when (inputUser) {
                    1 -> {
                        showMealDetailsViewer.viewDetails(suggestHighCalorieMealUseCase.getRandomHighCalorieMeal())
                        break
                    }

                    2 -> anotherSuggestionMealViewer.viewDetails(ANOTHER_SUGGESTION_MEAL_MESSAGE)

                    3 -> break

                    else -> throw InvalidInputNumberOfHighCalorieMeal(INVALID_INPUT_NUMBER_EXCEPTION)
                }
                counter++
            }while (counter <3)
        } catch (emptyException: EmptyRandomMealException) {
            exceptionViewer.viewExceptionMessage(emptyException)
        } catch (invalidInputException: InvalidInputNumberOfHighCalorieMeal) {
            exceptionViewer.viewExceptionMessage(invalidInputException)
        } catch (generalException: Exception) {
            exceptionViewer.viewExceptionMessage(generalException)
        }
    }

    companion object {
        const val ANOTHER_SUGGESTION_MEAL_MESSAGE = "Show Another Suggestion Meal"
        const val INPUT_USER_MESSAGE = "Input Your Choose Number : "
        const val INVALID_INPUT_NUMBER_EXCEPTION = "Input User Number is not in valid range (0..3) , please try again\n\n"
    }
}