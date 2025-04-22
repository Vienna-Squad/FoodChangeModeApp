package org.example

import org.example.dependencyinjection.appModule
import org.example.dependencyinjection.useCasesModule
import org.example.presentation.App
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {

    print("\u001B[H\u001B[2J")
    println("\u001B[1;36m" + "=".repeat(30))
    println(" FOOD CHANGE MOOD! ".padStart(23))
    println("=".repeat(30) + "\u001B[0m\n")
    startKoin { modules(appModule, useCasesModule) }
    App().start()
}