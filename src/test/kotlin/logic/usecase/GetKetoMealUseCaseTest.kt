package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetKetoMealUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

class GetKetoMealUseCaseTest {

    private lateinit var getKetoMealUseCase: GetKetoMealUseCase
    private val mealsRepository: MealsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getKetoMealUseCase = GetKetoMealUseCase(mealsRepository)
    }

    @Test
    fun `invoke should return null when no keto meals available`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createNonKetoMeal("Pasta"),
            createNonKetoMeal("Rice")
        )

        // When
        val result = getKetoMealUseCase.invoke(emptySet())

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `invoke should return keto meal when available`() {
        // Given
        val ketoMeal = createKetoMeal("Keto Steak")
        every { mealsRepository.getAllMeals() } returns listOf(
            createNonKetoMeal("Pasta"),
            ketoMeal,
            createNonKetoMeal("Rice")
        )

        // When
        val result = getKetoMealUseCase.invoke(emptySet())

        // Then
        assertThat(result).isEqualTo(ketoMeal)
    }

    @Test
    fun `invoke should exclude seen meals`() {
        // Given
        val seenKetoMeal = createKetoMeal("Seen Keto Meal")
        val newKetoMeal = createKetoMeal("New Keto Meal")
        every { mealsRepository.getAllMeals() } returns listOf(
            seenKetoMeal,
            newKetoMeal,
            createNonKetoMeal("Pasta")
        )

        // When
        val result = getKetoMealUseCase.invoke(setOf(seenKetoMeal))

        // Then
        assertThat(result).isEqualTo(newKetoMeal)
    }

    @Test
    fun `invoke should return null when all keto meals have been seen`() {
        // Given
        val ketoMeal1 = createKetoMeal("Keto Meal 1")
        val ketoMeal2 = createKetoMeal("Keto Meal 2")
        every { mealsRepository.getAllMeals() } returns listOf(
            ketoMeal1,
            ketoMeal2,
            createNonKetoMeal("Pasta")
        )

        // When
        val result = getKetoMealUseCase.invoke(setOf(ketoMeal1, ketoMeal2))

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `invoke should return null when nutrition data is missing`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMealWithMissingNutrition("Meal 1"),
            createMealWithMissingNutrition("Meal 2")
        )

        // When
        val result = getKetoMealUseCase.invoke(emptySet())

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `invoke should return null when carbs or fat data is missing`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMealWithPartialNutrition("Meal 1"),
            createMealWithPartialNutrition("Meal 2")
        )

        // When
        val result = getKetoMealUseCase.invoke(emptySet())

        // Then
        assertThat(result).isNull()
    }

    private fun createKetoMeal(name: String): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = null,
            nutrition = Nutrition(
                calories = null,
                totalFat = 25.0F,
                sugar = null,
                sodium = null,
                protein = null,
                saturatedFat = null,
                carbohydrates = 10.0F
            ),
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = null,
            numberOfIngredients = null
        )
    }

    private fun createNonKetoMeal(name: String): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = null,
            nutrition = Nutrition(
                calories = null,
                totalFat = 10.0F,
                sugar = null,
                sodium = null,
                protein = null,
                saturatedFat = null,
                carbohydrates = 50.0F
            ),
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = null,
            numberOfIngredients = null
        )
    }

    private fun createMealWithMissingNutrition(name: String): Meal {
        return Meal(
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
    }

    private fun createMealWithPartialNutrition(name: String): Meal {
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
                carbohydrates = 10.0F
            ),
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = null,
            numberOfIngredients = null
        )
    }
}