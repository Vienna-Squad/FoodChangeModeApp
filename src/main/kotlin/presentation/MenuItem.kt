package org.example.presentation

import org.example.logic.usecase.GuessIngredientGameUseCase
import org.example.presentation.controllers.EasyMealUIController
import org.example.presentation.controllers.EggFreeSweetsUIController
import org.example.presentation.controllers.ExitUIController
import org.example.presentation.controllers.HealthyFastFoodUIController
import org.example.presentation.controllers.HighCalorieMealUIController
import org.example.presentation.controllers.IngredientGuessGameUiController
import org.example.presentation.controllers.IraqiMealsUIController
import org.example.presentation.controllers.ItalianMealForGroupsUiController
import org.example.presentation.controllers.KetoMealsUiController
import org.example.presentation.controllers.MealByCountryUiController
import org.example.presentation.controllers.MealByNameUiController
import org.example.presentation.controllers.MealsByDateUiController
import org.example.presentation.controllers.MealsByProteinAndCaloriesUiController
import org.example.presentation.controllers.PotatoesMealsUiController
import org.example.presentation.controllers.PreparationTimeGuessingGameUiController
import org.example.presentation.controllers.RankedSeafoodByProteinUiController

enum class MenuItem(val title: String, private val uiController: UiController = ExitUIController()) {
    MEAL_BY_NAME("Find Meal By Name", MealByNameUiController()),
    MEAL_BY_COUNTRY("Explore Meals by Country", MealByCountryUiController()),
    MEAL_BY_DATE("Find Meals by Date", MealsByDateUiController()),
    IRAQI_MEALS("Discover Iraqi Meals", IraqiMealsUIController()),
    ITALIAN_MEAL_FOR_GROUPS("Discover Italian Meals For Large Groups", ItalianMealForGroupsUiController()),
    EASY_FOOD_SUGGESTION_GAME(
        "Suggest 10 Random Easy-To-Prepare Meals (Under 30 minutes preparation time, has 5 ingredients or fewer, and can be prepared in less than 6 steps)",
        EasyMealUIController()
    ),
    HEALTHY_FAST_FOOD(
        "Get Healthy Fast-Food Meal (Under 15 minutes preparation time, low total fat, saturated fat, and carbohydrate)",
        HealthyFastFoodUIController()
    ),
    KETO_DIET_MEAL("Discover Keto Meals", KetoMealsUiController()),
    EGG_FREE_SWEETS("Discover Egg-Free Sweets", EggFreeSweetsUIController()),
    HIGH_CALORIES_MEAL("High-Calorie Meals for Weight Gain", HighCalorieMealUIController()),
    CALCULATED_CALORIES_MEAL("Get Calorie-Tracked Meals", MealsByProteinAndCaloriesUiController()),
    POTATO_MEALS("Discover Potato-Based Dishes", PotatoesMealsUiController()),
    SEAFOOD_MEALS("Explore Seafood Dishes", RankedSeafoodByProteinUiController()),
    PREPARATION_TIME_GUESSING_GAME("Play Guess Meal Preparation Time", PreparationTimeGuessingGameUiController()),
    INGREDIENT_GAME("Play Guess the Meal Ingredient Game", IngredientGuessGameUiController()),
    EXIT("Exit");
    fun execute() = this.uiController.execute()
}