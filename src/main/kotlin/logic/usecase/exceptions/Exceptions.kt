package org.example.logic.usecase.exceptions

abstract class FoodChangeMoodException(message: String) : Exception(message)
class NoMealFoundException(mealType: String = "meals") : FoodChangeMoodException("No $mealType found")
class IngredientRandomMealGenerationException(message: String) : FoodChangeMoodException(message)
class IngredientUserInputException(message: String) : FoodChangeMoodException(message)
class IngredientsOptionsException(message: String) : FoodChangeMoodException(message)
class EmptyRandomMealException(message: String) : FoodChangeMoodException(message)
class InvalidInputNumberOfHighCalorieMeal(message: String) : FoodChangeMoodException(message)
class IncorrectDateFormatException(message: String) : FoodChangeMoodException(message)


open class GuessPrepareTimeGameException(val attempt: Int, message: String) : FoodChangeMoodException(message)
class InvalidMinutesException : GuessPrepareTimeGameException(0, INVALID_MINUTES_MESSAGE) {
    companion object {
        const val INVALID_MINUTES_MESSAGE =
            "Are you stupid!! There is no time to prepare a meal that is in the NEGATIVE or ZERO."
    }
}

class InvalidAttemptsException : GuessPrepareTimeGameException(0, INVALID_ATTEMPTS_MESSAGE) {
    companion object {
        const val INVALID_ATTEMPTS_MESSAGE = "You do not have enough attempts to start playing."
    }
}

class GameOverException(correctAnswer: Long) : GuessPrepareTimeGameException(0, "$GAME_OVER_MESSAGE $correctAnswer") {
    companion object {
        const val GAME_OVER_MESSAGE = "Unfortunately, the game has ended. The correct number of minutes was: "
    }
}

class TooHighException(attempt: Int) : GuessPrepareTimeGameException(attempt, TOO_HIGH_MESSAGE) {
    companion object {
        const val TOO_HIGH_MESSAGE = "Your guess is too high"
    }
}

class TooLowException(attempt: Int) : GuessPrepareTimeGameException(attempt, TOO_LOW_MESSAGE) {
    companion object {
        const val TOO_LOW_MESSAGE = "Your guess is too low"
    }
}