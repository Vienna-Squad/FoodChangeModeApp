package org.example.dependencyinjection

import org.example.logic.usecase.*
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.koin.dsl.module


val useCasesModule = module {
    single { GetEasyFoodSuggestionUseCase(get()) }
    single { GetIraqiMealsUseCase(get()) }
    single { GetItalianGroupMealsUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { GetMealByNameUseCase(get()) }
    single { GetMealsByDateUseCase(get()) }
    single { GetMealsByProteinAndCaloriesUseCase(get()) }
    single { GetMealsOfCountryUseCase(get(),get()) }
    single { GetRandomPotatoMealsUseCase(get()) }
    single { GetRankedSeafoodByProteinUseCase(get()) }
    single { GuessPrepareTimeGameUseCase(get()) }
    single { SuggestHighCalorieMealUseCase(get()) }
    single { GuessIngredientGameUseCase(get()) }
    single { GetEggFreeSweetsUseCase(get()) }
    single { GetHealthyFastFoodUseCase(get()) }

}