package org.example.utils

fun <T,E>Pair<T,E>.show(){
    println("Name : [ ${this.first} ]\nDescription : ${this.second}")
}

