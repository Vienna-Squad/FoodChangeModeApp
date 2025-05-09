package org.example.presentation.controllers

import org.example.logic.usecase.GuessPrepareTimeGameUseCase
import org.example.logic.usecase.exceptions.GuessPrepareTimeGameException
import org.example.logic.usecase.exceptions.InvalidMinutesException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.interactor.Interactor
import org.example.utils.interactor.UserInteractor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.MessageViewer
import org.koin.mp.KoinPlatform.getKoin

class PreparationTimeGuessingGameUiController(
    private val guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase = getKoin().get(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer(),
    private val messageViewer: ItemDetailsViewer<String> = MessageViewer(),
    private val interactor: Interactor = UserInteractor(),
) : UiController {
    override fun execute() {
        try {
            with(guessPrepareTimeGameUseCase.getRandomMeal()) {
                minutes?.let { minutes ->
                    print("guess its preparation time of $name: ")
                    var attempt = 3
                    while (true) {
                        val guessMinutes = interactor.getInput().toLongOrNull() ?: run {
                            exceptionViewer.viewExceptionMessage(InvalidMinutesException())
                            return
                        }
                        try {
                            messageViewer.viewDetails(guessPrepareTimeGameUseCase.guess(guessMinutes, minutes, attempt))
                            break
                        } catch (exception: GuessPrepareTimeGameException) {
                            attempt = exception.attempt
                            if (attempt == 0) {
                                exceptionViewer.viewExceptionMessage(exception)
                                break
                            }
                            messageViewer.viewDetails("${exception.message}, try again: ")
                        }
                    }
                } ?: run {
                    exceptionViewer.viewExceptionMessage(InvalidMinutesException())
                }
            }
        } catch (exception: NoMealFoundException) {
            exceptionViewer.viewExceptionMessage(exception)
        }
    }
}