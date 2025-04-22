package org.example.logic.usecase

import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.logic.usecase.exceptions.TooHighException
import org.example.logic.usecase.exceptions.TooLowException
import org.example.utils.getRandomIndex

class GuessPrepareTimeGameUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getRandomMeal() = mealsRepository.getAllMeals()
        .ifEmpty { throw NoMealFoundException() }
        .let { meals -> meals.getOrNull(meals.size.getRandomIndex()) ?: throw NoMealFoundException() }

    fun guess(guessMinutes: Long, correctMinutes: Long, leftAttempt: Int) = when {
        guessMinutes > correctMinutes -> if (leftAttempt == 1) "GameOver" else throw TooHighException(leftAttempt - 1)
        guessMinutes < correctMinutes -> if (leftAttempt == 1) "GameOver" else throw TooLowException(leftAttempt - 1)
        else -> "CorrectGuess"
    }
}


