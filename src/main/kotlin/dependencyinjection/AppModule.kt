package org.example.dependencyinjection

import org.example.logic.repository.MealsRepository
import org.koin.dsl.module
import java.io.File

val appModule= module {
    single {
        File("food.csv")
    }

}