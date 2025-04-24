package org.example.presentation.controllers

import org.example.logic.usecase.GuessPrepareTimeGameUseCase
import org.example.logic.usecase.exceptions.GameOverException
import org.example.logic.usecase.exceptions.GuessPrepareTimeGameException
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.logic.usecase.exceptions.TooHighException
import org.example.logic.usecase.exceptions.TooLowException
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.FoodExceptionViewer
import org.koin.mp.KoinPlatform.getKoin

class PreparationTimeGuessingGameUiController(
    private val guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase = getKoin().get(),
    private val exceptionViewer: ExceptionViewer = FoodExceptionViewer()
) : UiController {
    override fun execute() {
        try {
            with(guessPrepareTimeGameUseCase.getRandomMeal()) {
                minutes?.let { minutes ->
                    print("guess its preparation time of $name: ")
                    var attempt = 3
                    while (true) {
                        val guessMinutes = readln().toLongOrNull() ?: -1
                        try {
                            println(guessPrepareTimeGameUseCase.guess(guessMinutes, minutes, attempt))
                            break
                        }catch (exception: GuessPrepareTimeGameException){
                            attempt = exception.attempt
                            if (attempt == 0){
                                exceptionViewer.viewExceptionMessage(exception)
                                break
                            }
                            print("${exception.message} try again: ")
                        }

                        /*catch (exception: TooHighException) {
                            attempt = exception.attempt
                            print("${exception.message} try again: ")
                        } catch (exception: TooLowException) {
                            attempt = exception.attempt
                            print("${exception.message} try again: ")
                        } catch (exception: GameOverException) {
                            exceptionViewer.viewExceptionMessage(exception)
                            break
                        }*/
                    }
                }
            }
        } catch (exception: NoMealFoundException) {
            exceptionViewer.viewExceptionMessage(exception)
        }
    }
}