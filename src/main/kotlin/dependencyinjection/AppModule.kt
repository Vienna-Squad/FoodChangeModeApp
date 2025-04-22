package org.example.dependencyinjection

import org.example.data.CsvFileReader
import org.example.data.CsvMealsRepository
import org.example.data.MealsCsvParser
import org.example.logic.repository.MealsRepository
import org.example.presentation.App
import org.example.utils.KMPSearcher
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single { File("food.csv") }
    single { CsvFileReader(mealsFile = get()) }
    single { MealsCsvParser() }
    single { KMPSearcher() }
    single<MealsRepository> { CsvMealsRepository(csvFileReader = get(), mealsCsvParser = get()) }
    single {
        App()
    }
}