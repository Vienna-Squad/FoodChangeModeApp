package org.example.logic.usecase.exceptions

import org.koin.core.logger.MESSAGE

abstract class FoodChangeMoodException(message: String) : Exception(message)
class IncorrectDateFormatException(message: String) : FoodChangeMoodException(message)
class MealsNotFoundForThisDateException(message: String) : FoodChangeMoodException(message)
class NoMealFoundByNameException(message: String) : FoodChangeMoodException(message)
class NotACountryException(message: String) : FoodChangeMoodException(message)
class NoItalianGroupMealsException(message: String) : FoodChangeMoodException(message)
open class GuessPrepareTimeGameException(val attempt: Int, message: String) : FoodChangeMoodException(message)
class TooHighException(attempt: Int) : GuessPrepareTimeGameException(attempt, "TooHigh")
class TooLowException(attempt: Int) : GuessPrepareTimeGameException(attempt, "TooLow")
class NullRandomMealException(message: String): FoodChangeMoodException(message)
class InvalidInputNumberOfHighCalorieMeal(message: String): FoodChangeMoodException(message)
class IngredientRandomMealGenerationException(message: String) : FoodChangeMoodException(message)
class IngredientUserInputException(message: String) : FoodChangeMoodException(message)
class IngredientsOptionsException(message: String) : FoodChangeMoodException(message)