package org.example.utils.interactor

import org.example.logic.usecase.exceptions.InvalidInputNumberOfHighCalorieMeal

class UserHighCalorieMealInteractor: HighCalorieMealInteractor {
    override fun getInput(): Int  = readln().toIntOrNull()
        ?: throw InvalidInputNumberOfHighCalorieMeal("The Input User Is Not Number")
}