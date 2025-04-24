package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.junit.jupiter.api.BeforeEach
import java.util.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetItalianGroupMealsUseCaseTest {

    private lateinit var mealsRepository: MealsRepository
    private lateinit var getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase

    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        getItalianGroupMealsUseCase = GetItalianGroupMealsUseCase(mealsRepository)
    }


    @Test
    fun `should return meal when it has both italian and for-large-groups tags`() {
        // Given
        val meal = createMeal(
            name = "Pizza",
            tags = listOf("italian", "for-large-groups"),
            description = "A classic Italian dish",
            ingredients = listOf("dough", "cheese")
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When
        val result = getItalianGroupMealsUseCase()

        // Then
        assertThat(result).containsExactly(meal)
    }

    @Test
    fun `should throw NoMealFoundException when meal does not have both required tags`() {
        // Given
        val meal = createMeal(
            name = "Sushi",
            tags = listOf("japanese"),
            description = "A Japanese dish",
            minutes = 60L,
            nutrition = Nutrition(
                calories = 500f,
                totalFat = 10f,
                sugar = 2f,
                sodium = 600f,
                protein = 25f,
                saturatedFat = 2f,
                carbohydrates = 80f
            ),
            numberOfSteps = 6,
            ingredients = listOf("rice", "fish")
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoMealFoundException> {
            getItalianGroupMealsUseCase()
        }
    }

    @Test
    fun `should throw NoMealFoundException when meal has only one of the required tags`() {
        // Given
        val meal = createMeal(
            name = "Burger",
            tags = listOf("american", "for-large-groups"),
            description = "An American burger",
            minutes = 30L,
            nutrition = Nutrition(
                calories = 600f,
                totalFat = 20f,
                sugar = 3f,
                sodium = 800f,
                protein = 30f,
                saturatedFat = 8f,
                carbohydrates = 50f
            ),
            numberOfSteps = 4,
            ingredients = listOf("bun", "meat")
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoMealFoundException> {
            getItalianGroupMealsUseCase()
        }
    }

    @Test
    fun `should throw NoMealFoundException when tags are null`() {
        // Given
        val meal = createMeal(
            name = "Sushi",
            tags = null,
            description = "A Japanese dish",
            minutes = 60L,
            nutrition = Nutrition(
                calories = 500f,
                totalFat = 10f,
                sugar = 2f,
                sodium = 600f,
                protein = 25f,
                saturatedFat = 2f,
                carbohydrates = 80f
            ),
            numberOfSteps = 6,
            ingredients = listOf("rice", "fish")
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoMealFoundException> {
            getItalianGroupMealsUseCase()
        }
    }

    @Test
    fun `should throw NoMealFoundException when tags are empty`() {
        // Given
        val meal = createMeal(
            name = "Sushi",
            tags = emptyList(),
            description = "A Japanese dish",
            minutes = 60L,
            nutrition = Nutrition(
                calories = 500f,
                totalFat = 10f,
                sugar = 2f,
                sodium = 600f,
                protein = 25f,
                saturatedFat = 2f,
                carbohydrates = 80f
            ),
            numberOfSteps = 6,
            ingredients = listOf("rice", "fish")
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoMealFoundException> {
            getItalianGroupMealsUseCase()
        }
    }

    @Test
    fun `should return meal when tags have different case`() {
        // Given
        val meal = createMeal(
            name = "Pizza",
            tags = listOf("ITALIAN", "For-Large-Groups"),
            description = "A classic Italian dish",
            ingredients = listOf("dough", "cheese")
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When
        val result = getItalianGroupMealsUseCase()

        // Then
        assertThat(result).containsExactly(meal)
    }


    private fun createMeal(
        name: String,
        id: Long = 1L,
        tags: List<String>? = null,
        description: String? = null,
        minutes: Long = 45L,
        contributorId: Long = 101L,
        nutrition: Nutrition = Nutrition(
            calories = 700f,
            totalFat = 15f,
            sugar = 5f,
            sodium = 300f,
            protein = 20f,
            saturatedFat = 3f,
            carbohydrates = 120f
        ),
        numberOfSteps: Int = 5,
        steps: List<String> = listOf("Step 1", "Step 2"),
        ingredients: List<String> = listOf("ingredient1", "ingredient2"),
        numberOfIngredients: Int = 2
    ): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = minutes,
            contributorId = contributorId,
            submitted = Date(),
            tags = tags,
            nutrition = nutrition,
            numberOfSteps = numberOfSteps,
            steps = steps,
            description = description,
            ingredients = ingredients,
            numberOfIngredients = numberOfIngredients
        )
    }
}