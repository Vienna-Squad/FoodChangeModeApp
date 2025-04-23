package org.example.utils

import org.example.logic.model.Meal

fun Meal.showNameAndDescription(){
    println("Name : [ ${this.name} ]\nDescription : ${this.description}")
}

