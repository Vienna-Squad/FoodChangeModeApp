package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetKetoMealUseCase
import buildKetoMeal
import buildMealWithMissingNutrition
import buildMealWithPartialNutrition
import buildNonKetoMeal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetKetoMealUseCaseTest {

    private lateinit var getKetoMealUseCase: GetKetoMealUseCase
    private val mealsRepository: MealsRepository = mockk()

    @BeforeEach
    fun setUp() {
        getKetoMealUseCase = GetKetoMealUseCase(mealsRepository)
    }

    @Test
    fun `invoke should return null when no keto meals available`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            buildNonKetoMeal("Pasta"),
            buildNonKetoMeal("Rice")
        )

        // When
        val result = getKetoMealUseCase.invoke(emptySet())

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `invoke should return keto meal when available`() {
        // Given
        val ketoMeal = buildKetoMeal("Keto Steak")
        every { mealsRepository.getAllMeals() } returns listOf(
            buildNonKetoMeal("Pasta"),
            ketoMeal,
            buildNonKetoMeal("Rice")
        )

        // When
        val result = getKetoMealUseCase.invoke(emptySet())

        // Then
        assertThat(result).isEqualTo(ketoMeal)
    }

    @Test
    fun `invoke should exclude seen meals`() {
        // Given
        val seenKetoMeal = buildKetoMeal("Seen Keto Meal")
        val newKetoMeal = buildKetoMeal("New Keto Meal")
        every { mealsRepository.getAllMeals() } returns listOf(
            seenKetoMeal,
            newKetoMeal,
            buildNonKetoMeal("Pasta")
        )

        // When
        val result = getKetoMealUseCase.invoke(setOf(seenKetoMeal))

        // Then
        assertThat(result).isEqualTo(newKetoMeal)
    }

    @Test
    fun `invoke should return null when all keto meals have been seen`() {
        // Given
        val ketoMeal1 = buildKetoMeal("Keto Meal 1")
        val ketoMeal2 = buildKetoMeal("Keto Meal 2")
        every { mealsRepository.getAllMeals() } returns listOf(
            ketoMeal1,
            ketoMeal2,
            buildNonKetoMeal("Pasta")
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
            buildMealWithMissingNutrition("Meal 1"),
            buildMealWithMissingNutrition("Meal 2")
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
            buildMealWithPartialNutrition("Meal 1", hasFat = false, hasCarbs = true),
            buildMealWithPartialNutrition("Meal 2", hasFat = true, hasCarbs = false)
        )

        // When
        val result = getKetoMealUseCase.invoke(emptySet())

        // Then
        assertThat(result).isNull()
    }
}
