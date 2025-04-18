package org.example.utils

enum class MenuItem(val title: String) {
    // 1. Meal Discovery (General)
    MEAL_BY_NAME("Find Meal By Name"),
    MEAL_BY_COUNTRY("Explore Meals by Country"),
    MEAL_BY_DATE("Find Meals by Date"),
    IRAQI_MEALS("Discover Iraqi Meals"),
    ITALIAN_MEAL_FOR_GROUPS("Discover Italian Meals For Large Groups"),
    EASY_FOOD_SUGGESTION_GAME("Suggest 10 Random Easy-To-Prepare Meals (Under 30 minutes preparation time, has 5 ingredients or fewer, and can be prepared in less than 6 steps)"),
    // 2. Dietary-Specific Meals
    HEALTHY_FAST_FOOD("Get Healthy Fast-Food Meal (Under 15 minutes preparation time, low total fat, saturated fat, and carbohydrate)"),
    KETO_DIET_MEAL("Discover Keto Meals"),
    EGG_FREE_SWEETS("Discover Egg-Free Sweets"),
    HIGH_CALORIES_MEAL("High-Calorie Meals for Weight Gain"),
    CALCULATED_CALORIES_MEAL("Get Calorie-Tracked Meals"),

    // 3. Ingredient-Focused Meals
    POTATO_MEALS("Discover Potato-Based Dishes"),
    SEAFOOD_MEALS("Explore Seafood Dishes"),

    // 4. Interactive Features
    PREPARATION_TIME_GUESSING_GAME("Play Guess Meal Preparation Time"),
    INGREDIENT_GAME("Play Guess the Meal Ingredient Game"),

    // 6. System Option
    EXIT("Exit");
}

fun Int.toMenuItem() = MenuItem.entries.getOrNull(this - 1) ?: MenuItem.EXIT