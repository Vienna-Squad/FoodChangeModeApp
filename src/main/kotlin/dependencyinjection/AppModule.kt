package org.example.dependencyinjection

import org.example.data.CsvFileReader
import org.example.data.CsvMealsRepository
import org.example.data.MealsCsvParser
import org.example.logic.repository.MealsRepository
import org.koin.dsl.module
import java.io.File

val appModule= module {
    single {
        File("food.csv")
    }
    single <MealsRepository>{
        CsvMealsRepository(get(),get())
    }
    single {
        MealsCsvParser()
    }
    single {
        CsvFileReader(get())
    }

}