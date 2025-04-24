package org.example.logic.usecase

import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.exceptions.*
import org.example.utils.getRandomItem

class GuessPrepareTimeGameUseCase(
    private val mealsRepository: MealsRepository
) {
    fun getRandomMeal() = mealsRepository.getAllMeals()
        .let { meals -> meals.getRandomItem() ?: throw NoMealFoundException() }

    fun guess(guessMinutes: Long, correctMinutes: Long, leftAttempt: Int): String {
        return when {
            leftAttempt <= 0 -> throw InvalidAttemptsException()
            guessMinutes < 0 -> throw InvalidMinutesException()
            guessMinutes > correctMinutes -> if (leftAttempt == 1) throw GameOverException(correctMinutes) else throw TooHighException(
                leftAttempt - 1
            )

            guessMinutes < correctMinutes -> if (leftAttempt == 1) throw GameOverException(correctMinutes) else throw TooLowException(
                leftAttempt - 1
            )

            else -> CONGRATULATION_MESSAGE
        }
    }

    companion object {
        const val CONGRATULATION_MESSAGE = "Congratulation!! it's a correct guess"
    }
}


