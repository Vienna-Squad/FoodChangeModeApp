package org.example

import di.appModule
import di.useCasesModule
import org.example.presentation.App
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    print("\u001B[H\u001B[2J")
    println("\u001B[1;36m" + "=".repeat(30))
    println(" FOOD CHANGE MOOD! ".padStart(23))
    println("=".repeat(30) + "\u001B[0m")
    startKoin { modules(appModule, useCasesModule) }
    App().start()
}