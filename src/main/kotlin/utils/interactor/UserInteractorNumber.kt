package org.example.utils.interactor

import org.example.logic.usecase.exceptions.IngredientUserInputException

class UserInteractorNumber: InteractorNumber {
    override fun getInput(): Int = readln().toIntOrNull()
    ?: throw IngredientUserInputException(INVALID_INPUT_TYPE_MESSAGE)
    companion object {
        const val INVALID_INPUT_TYPE_MESSAGE = " the input value is not an number"
    }
}