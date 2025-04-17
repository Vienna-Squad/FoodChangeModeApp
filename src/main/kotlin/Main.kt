package org.example

import org.example.dependencyinjection.appModule
import org.example.dependencyinjection.useCasesModule
import org.example.presentation.App
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin
fun main() {
    println("=================================FoodChangeMood!=================================")
    startKoin { modules(appModule, useCasesModule) }
    val app: App = getKoin().get()
    app.start()
}