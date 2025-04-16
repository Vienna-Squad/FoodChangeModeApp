package org.example.logic.usecase.exceptions

abstract class FoodChangeMood(message: String) : Exception(message)
class IncorrectDateFormatException(message: String) : FoodChangeMood(message)
class MealsNotFoundForThisDateException(message: String) : FoodChangeMood(message)
class NoMealFoundByNameException(message:String):FoodChangeMood(message)
open class GuessPrepareTimeGameException(val attempt: Int, message: String) : FoodChangeMood(message)
class TooHighException(attempt: Int) : GuessPrepareTimeGameException(attempt, "TooHigh")
class TooLowException(attempt: Int) : GuessPrepareTimeGameException(attempt, "TooLow")