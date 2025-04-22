package org.example.presentation.controllers

import org.example.logic.usecase.GuessPrepareTimeGameUseCase
import org.example.logic.usecase.exceptions.GuessPrepareTimeGameException
import org.example.presentation.UiController
import org.koin.mp.KoinPlatform.getKoin

class PreparationTimeGuessingGameUiController(
    private val guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase = getKoin().get()
) : UiController {
    override fun execute() {
        with(guessPrepareTimeGameUseCase.getMeal()) {
            minutes?.let { minutes ->
                var attempt = 3
                print("guess its preparation time of $name: ")
                while (true) {
                    val guessMinutes = readln().toLongOrNull() ?: -1
                    try {
                        println(guessPrepareTimeGameUseCase.guess(guessMinutes, minutes, attempt))
                        break
                    } catch (exception: GuessPrepareTimeGameException) {
                        attempt = exception.attempt
                        print("${exception.message} try again: ")
                    }
                }
            }
        }
    }
}