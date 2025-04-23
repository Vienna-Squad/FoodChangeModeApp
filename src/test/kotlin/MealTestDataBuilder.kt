import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import java.util.Date

fun createMeal(name: String?, ingredients: List<String>?): Meal {
    return Meal(
        name = name,
        id = null,
        minutes = null,
        contributorId = null,
        submitted = Date(),
        tags = null,
        nutrition = Nutrition(
            calories = null,
            totalFat = null,
            sugar = null,
            sodium = null,
            protein = null,
            saturatedFat = null,
            carbohydrates = null
        ),
        numberOfSteps = null,
        steps = null,
        description = null,
        ingredients = ingredients,
        numberOfIngredients = ingredients?.size
    )
}

fun createPotatoMeal(id: Int): Meal {
    return createMeal(
        name = "Potato Meal $id",
        ingredients = listOf("potato", "salt")
    )
}

fun createNonPotatoMeal(name: String): Meal {
    return createMeal(
        name = name,
        ingredients = listOf("lettuce", "tomato")
    )
}