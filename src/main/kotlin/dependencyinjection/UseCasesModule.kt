package org.example.dependencyinjection

import org.example.logic.usecase.*
import org.koin.dsl.module
import kotlin.math.sin

val useCasesModule = module {
    single { GetEasyFoodSuggestionUseCase(get()) }
    single { GetIraqiMealsUseCase(get()) }
    single { GetItalianGroupMealsUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { GetMealByName(get()) }
    single { GetMealsByDateUseCase(get()) }
    single { GetMealsByProteinAndCaloriesUseCase(get()) }
    single { GetMealsOfCountryUseCase(get()) }
    single { GetRandomPotatoMealsUseCase(get()) }
    single { GetRankedSeafoodByProteinUseCase(get()) }
    single { GuessPrepareTimeGameUseCase(get()) }
    single { SuggestHighCalorieMealUseCase(get()) }
}