package org.example.logic.usecase

import org.example.logic.repository.MealsRepository

class GuessPrepareTimeGameUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getMeal() = mealsRepository.getAllMeals().random()
    fun guess(guessMinutes: Long, correctMinutes: Long, leftAttempt: Int) = when {
        guessMinutes > correctMinutes -> if (leftAttempt == 1) "GameOver" else throw TooHighException(leftAttempt - 1)
        guessMinutes < correctMinutes -> if (leftAttempt == 1) "GameOver" else throw TooLowException(leftAttempt - 1)
        else -> "CorrectGuess"
    }
}

open class GuessPrepareTimeGameException(val attempt: Int, message: String) : Exception(message)
class TooHighException(attempt: Int) : GuessPrepareTimeGameException(attempt, "TooHigh")
class TooLowException(attempt: Int) : GuessPrepareTimeGameException(attempt, "TooLow")
