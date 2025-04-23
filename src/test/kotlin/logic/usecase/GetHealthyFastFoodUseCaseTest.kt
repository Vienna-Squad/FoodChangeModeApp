package logic.usecase

import org.junit.jupiter.api.Test
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import io.mockk.every
import io.mockk.mockk
import com.google.common.truth.Truth.assertThat
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.example.logic.usecase.exceptions.NoHealthyFastFoodFoundException
import org.junit.jupiter.api.assertThrows

class GetHealthyFastFoodUseCaseTest {

    private val mockRepository = mockk<MealsRepository>()
    private val useCase = GetHealthyFastFoodUseCase(mockRepository)

    @Test
    fun `invoke should throw when no meals exist`() {
        every { mockRepository.getAllMeals() } returns emptyList()

        val exception = assertThrows<NoHealthyFastFoodFoundException> {
            useCase()
        }

        assertThat(exception.message).isEqualTo("No meals available in repository")
    }

    @Test
    fun `invoke should throw when no meals have nutrition data`() {
        val meals = listOf(
            createMealWithNullNutrition("Meal1", 10),
            createMealWithNullNutrition("Meal2", 12)
        )

        every { mockRepository.getAllMeals() } returns meals

        val exception = assertThrows<NoHealthyFastFoodFoundException> {
            useCase()
        }

        assertThat(exception.message).isEqualTo("No meals with nutrition data available")
    }



    @Test
    fun `invoke should throw when no meals meet healthy criteria`() {
        val meals = listOf(
            createHealthyFastFoodMeal("High Fat Meal", 20, 20f, 6f, 40f),
            createHealthyFastFoodMeal("Slow Meal", 30, 5f, 1f, 10f)
        )

        every { mockRepository.getAllMeals() } returns meals

        assertThrows<NoHealthyFastFoodFoundException> {
            useCase()
        }
    }

    @Test
    fun `invoke should return only meals meeting all healthy fast food criteria`() {
        val meals = listOf(
            // Healthy fast food (meets all criteria)
            createHealthyFastFoodMeal("Quick Salad", 10, 5f, 1f, 10f),
            // Not fast (prep time > 15 mins)
            createHealthyFastFoodMeal("Slow Soup", 20, 5f, 1f, 10f),
            // High fat
            createHealthyFastFoodMeal("Fatty Fish", 10, 25f, 1f, 10f),
            // High saturated fat
            createHealthyFastFoodMeal("Buttery Pasta", 10, 5f, 8f, 10f),
            // High carbs
            createHealthyFastFoodMeal("Sweet Dessert", 10, 5f, 1f, 45f)
        )

        every { mockRepository.getAllMeals() } returns meals

        val result = useCase()

        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Quick Salad")
    }


    @Test
    fun `invoke should return multiple meals when they meet criteria`() {
        val meals = listOf(
            // These will set the averages (totalFat=10, saturatedFat=3, carbs=20)
            createHealthyFastFoodMeal("High Fat Meal", 20, 20f, 6f, 40f),
            createHealthyFastFoodMeal("High Saturated Meal", 20, 20f, 6f, 40f),

            // These should be included (below average)
            createHealthyFastFoodMeal("Salad", 10, 5f, 1f, 10f),
            createHealthyFastFoodMeal("Grilled Fish", 12, 6f, 2f, 8f),
            createHealthyFastFoodMeal("Veggie Wrap", 15, 7f, 1.5f, 12f)
        )

        every { mockRepository.getAllMeals() } returns meals

        val result = useCase()

        assertThat(result).hasSize(3)
        assertThat(result.map { it.name }).containsExactly("Salad", "Grilled Fish", "Veggie Wrap")
    }

    @Test
    fun `invoke should compare fat values against average`() {
        val meals = listOf(
            // Average values: totalFat=15, saturatedFat=5, carbs=20
            createHealthyFastFoodMeal("Avg Meal", 20, 15f, 5f, 20f),
            // Below average values
            createHealthyFastFoodMeal("Healthy Meal", 10, 10f, 3f, 15f),
            // Above average values
            createHealthyFastFoodMeal("Unhealthy Meal", 10, 20f, 8f, 30f)
        )

        every { mockRepository.getAllMeals() } returns meals

        val result = useCase()

        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Healthy Meal")
    }

    @Test
    fun `invoke should filter based on average values`() {
        // Given - setup averages of 30f fat, 15f saturated fat, 40f carbs
        val healthyMeal = createHealthyFastFoodMeal(
            minutes = 10,
            totalFat = 25f, // below average
            saturatedFat = 10f, // below average
            carbs = 35f // below average
        )

        every { mockRepository.getAllMeals() } returns listOf(
            healthyMeal,
            createHealthyFastFoodMeal(minutes = 10, totalFat = 35f, saturatedFat = 10f, carbs = 35f), // fat too high
            createHealthyFastFoodMeal(minutes = 10, totalFat = 25f, saturatedFat = 20f, carbs = 35f), // saturated fat too high
            createHealthyFastFoodMeal(minutes = 10, totalFat = 25f, saturatedFat = 10f, carbs = 45f) // carbs too high
        )

        // When
        val result = useCase()

        // Then
        assertThat(result).containsExactly(healthyMeal)
    }


    private fun createMealWithNullNutrition(name: String, minutes: Long): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = minutes,
            contributorId = null,
            submitted = java.util.Date(),
            tags = null,
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = null,
            numberOfIngredients = null
        )
    }


    private fun createHealthyFastFoodMeal(
        name: String = "Test Meal",
        minutes: Long = 15,
        totalFat: Float = 20f,
        saturatedFat: Float = 5f,
        carbs: Float = 25f
    ): Meal {
        return Meal(
            name = name,
            nutrition = Nutrition(
                calories = 500f,
                totalFat = totalFat,
                protein = 30f,
                sugar = 5f,
                sodium = 200f,
                saturatedFat = saturatedFat,
                carbohydrates = carbs
            ),
            id = 1,
            minutes = minutes,
            contributorId = 1,
            submitted = java.util.Date(),
            tags = listOf("test"),
            numberOfSteps = 1,
            steps = listOf("Step 1"),
            description = "Test meal",
            ingredients = listOf("Ingredient 1"),
            numberOfIngredients = 1
        )
    }
}