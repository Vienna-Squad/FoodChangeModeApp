import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import java.util.Date

fun buildKetoMeal(
    name: String = "Keto Meal",
    totalFat: Float = 25.0f,
    carbs: Float = 10.0f,
    ingredients: List<String>? = null
): Meal = Meal(
    name = name,
    id = null,
    minutes = null,
    contributorId = null,
    submitted = Date(),
    tags = null,
    nutrition = Nutrition(
        calories = null,
        totalFat = totalFat,
        sugar = null,
        sodium = null,
        protein = null,
        saturatedFat = null,
        carbohydrates = carbs
    ),
    numberOfSteps = null,
    steps = null,
    description = null,
    ingredients = ingredients,
    numberOfIngredients = ingredients?.size
)

fun buildNonKetoMeal(
    name: String = "Non-Keto Meal",
    totalFat: Float = 10.0f,
    carbs: Float = 50.0f
): Meal = buildKetoMeal(name, totalFat, carbs)

fun buildMealWithMissingNutrition(name: String = "No Nutrition Meal"): Meal =
    Meal(
        name = name,
        id = null,
        minutes = null,
        contributorId = null,
        submitted = Date(),
        tags = null,
        nutrition = null,
        numberOfSteps = null,
        steps = null,
        description = null,
        ingredients = null,
        numberOfIngredients = null
    )

fun buildMealWithPartialNutrition(
    name: String = "Partial Nutrition Meal",
    hasFat: Boolean = false,
    hasCarbs: Boolean = true
): Meal = Meal(
    name = name,
    id = null,
    minutes = null,
    contributorId = null,
    submitted = Date(),
    tags = null,
    nutrition = Nutrition(
        calories = null,
        totalFat = if (hasFat) 25.0f else null,
        sugar = null,
        sodium = null,
        protein = null,
        saturatedFat = null,
        carbohydrates = if (hasCarbs) 10.0f else null
    ),
    numberOfSteps = null,
    steps = null,
    description = null,
    ingredients = null,
    numberOfIngredients = null
)