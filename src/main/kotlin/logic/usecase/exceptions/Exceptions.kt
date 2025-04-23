package org.example.logic.usecase.exceptions

abstract class FoodChangeMoodException(message: String) : Exception(message)
class NoMealFoundException(mealType: String = "meals") : FoodChangeMoodException("no $mealType found")
class IngredientRandomMealGenerationException(message: String) : FoodChangeMoodException(message)
class IngredientUserInputException(message: String) : FoodChangeMoodException(message)
class IngredientsOptionsException(message: String) : FoodChangeMoodException(message)
class EmptyRandomMealException(message: String): FoodChangeMoodException(message)
class InvalidInputNumberOfHighCalorieMeal(message: String): FoodChangeMoodException(message)
class IncorrectDateFormatException(message: String) : FoodChangeMoodException(message)



open class GuessPrepareTimeGameException(val attempt: Int, message: String) : FoodChangeMoodException(message)
class GameOverException : GuessPrepareTimeGameException(0, "game over")
class TooHighException(attempt: Int) : GuessPrepareTimeGameException(attempt, "too high")
class TooLowException(attempt: Int) : GuessPrepareTimeGameException(attempt, "too low")