package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GuessIngredientGameUseCase
import org.example.logic.usecase.IngredientGameDetails
import org.example.logic.usecase.exceptions.EmptyRandomMealException
import org.example.logic.usecase.exceptions.IngredientRandomMealGenerationException
import org.example.logic.usecase.exceptions.IngredientUserInputException
import org.example.logic.usecase.exceptions.IngredientsOptionsException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date
import kotlin.test.assertEquals

class GuessIngredientGameUseCaseTest {

    private val mealsRepository: MealsRepository = mockk(relaxed = true)
    lateinit var guessIngredientGameUseCase: GuessIngredientGameUseCase

    @BeforeEach
    fun setUp() {
        guessIngredientGameUseCase = GuessIngredientGameUseCase(mealsRepository)
    }

    @Test
    fun `getGameDetails should throw IngredientRandomMealGenerationException when a random meal is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns emptyList()
        // when & then
        assertThrows<IngredientRandomMealGenerationException> {
            guessIngredientGameUseCase.getGameDetails()
        }
    }

    @Test
    fun `getGameDetails should throw EmptyRandomMealException when the list of ingredient is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns mealsThatContainOnlyRandomMealsWithOneIngredientItem
        // when & then
        assertThrows<EmptyRandomMealException> {
            guessIngredientGameUseCase.getGameDetails()
        }
    }

    @Test
    fun `getGameDetails should return IngredientGameDetails when the random meal and ingredient options is valid`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns correctMeals
        val correctRandomMealName = correctMeals.random().name

        // when
        val result = guessIngredientGameUseCase.getGameDetails()

        assertThat(result.mealName).isEqualTo(correctRandomMealName)
    }

    @Test
    fun `guessGame should throw IngredientUserInputException when the ingredientInputNumber is not in range from 1 to 3`() {

        // given
        val input = 4
        val ingredientGameDetails = IngredientGameDetails(
            mealName = "Chicken",
            ingredients = listOf("egg", "egg", "egg")
            //                    1        2    3
        )

        // then
        assertThrows<IngredientUserInputException> {
            guessIngredientGameUseCase.guessGame(ingredientGameDetails, input)
        }


    }


    @Test
    fun `guessGame should throw IngredientsOptionsException when the input ingredient is wrong`() {

        // stubs
        every { mealsRepository.getAllMeals() } returns correctMeals

        // given
        val inputOption = 2
        val ingredientGameDetails = IngredientGameDetails(
            mealName = "Grilled Chicken Salad",
            ingredients = listOf("Chicken breast", "fake potato", "fake egg")
        )

        // then
        assertThrows<IngredientsOptionsException> {
            guessIngredientGameUseCase.guessGame(ingredientGameDetails, inputOption)
        }

    }

    @Test
    fun `guessGame should return true if userGuess is true`() {

        // stubs
        every { mealsRepository.getAllMeals() } returns correctMeals

        // given
        val inputOption = 1
        val ingredientGameDetails = IngredientGameDetails(
            mealName = "Grilled Chicken Salad",
            ingredients = listOf("Chicken breast", "fake potato", "fake egg")
        )
        // when
        val result = guessIngredientGameUseCase.guessGame(ingredientGameDetails, inputOption)

        assertTrue(result)

    }


}

val mealsThatContainOnlyRandomMealsWithOneIngredientItem = listOf(
    Meal(
        name = "Pasta Alfredo",
        id = 2L,
        minutes = 30L,
        contributorId = 102L,
        submitted = Date(),
        tags = listOf("comfort", "cheesy"),
        nutrition = Nutrition(
            calories = 600f,
            totalFat = 22f,
            sugar = 3f,
            sodium = 700f,
            protein = 15f,
            saturatedFat = 10f,
            carbohydrates = 70f
        ),
        numberOfSteps = 4,
        steps = listOf("Boil pasta", "Prepare sauce", "Mix pasta with sauce", "Serve hot"),
        description = "Creamy and delicious pasta alfredo.",
        ingredients = listOf("cheese"),
        numberOfIngredients = 1
    ), Meal(
        name = "Pasta Alfredo",
        id = 2L,
        minutes = 30L,
        contributorId = 102L,
        submitted = Date(),
        tags = listOf("comfort", "cheesy"),
        nutrition = Nutrition(
            calories = 600f,
            totalFat = 22f,
            sugar = 3f,
            sodium = 700f,
            protein = 15f,
            saturatedFat = 10f,
            carbohydrates = 70f
        ),
        numberOfSteps = 4,
        steps = listOf("Boil pasta", "Prepare sauce", "Mix pasta with sauce", "Serve hot"),
        description = "Creamy and delicious pasta alfredo.",
        ingredients = listOf("Pasta"),
        numberOfIngredients = 1
    )
)
val correctMeals = listOf(
    Meal(
        name = "Grilled Chicken Salad",
        id = 1L,
        minutes = 20L,
        contributorId = 101L,
        submitted = Date(),
        tags = listOf("healthy", "low-carb"),
        nutrition = Nutrition(
            calories = 250f,
            totalFat = 8f,
            sugar = 5f,
            sodium = 400f,
            protein = 30f,
            saturatedFat = 2f,
            carbohydrates = 10f
        ),
        numberOfSteps = 3,
        steps = listOf("Grill chicken", "Chop veggies", "Mix all"),
        description = "A healthy and fresh grilled chicken salad.",
        ingredients = listOf("Chicken breast", "Chicken breast", "Chicken breast"),
        numberOfIngredients = 1
    ),
    Meal(
        name = "Grilled Chicken Salad",
        id = 1L,
        minutes = 20L,
        contributorId = 101L,
        submitted = Date(),
        tags = listOf("healthy", "low-carb"),
        nutrition = Nutrition(
            calories = 250f,
            totalFat = 8f,
            sugar = 5f,
            sodium = 400f,
            protein = 30f,
            saturatedFat = 2f,
            carbohydrates = 10f
        ),
        numberOfSteps = 3,
        steps = listOf("Grill chicken", "Chop veggies", "Mix all"),
        description = "A healthy and fresh grilled chicken salad.",
        ingredients = listOf("Chicken breast", "Chicken breast", "Chicken breast"),
        numberOfIngredients = 1
    )
)

