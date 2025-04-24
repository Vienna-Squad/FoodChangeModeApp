package logic.usecase

import com.google.common.truth.Truth.assertThat
import createMeal
import createNonPotatoMeal
import createPotatoMeal
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetRandomPotatoMealsUseCase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRandomPotatoMealsUseCaseTest {

    private lateinit var getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase
    private val mealsRepository: MealsRepository = mockk()

    @BeforeEach
    fun setUp() {
        getRandomPotatoMealsUseCase = GetRandomPotatoMealsUseCase(
            mealsRepository = mealsRepository
        )
    }

    @Test
    fun `should return empty list when no meals contain potato`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createNonPotatoMeal("Salad"),
            createNonPotatoMeal("Beef")
        )

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return all meals when potato meals are 10 or less`() {
        // Given
        val potatoMeals = (1..5).map { createPotatoMeal(it) }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result).containsExactlyElementsIn(potatoMeals)
    }

    @Test
    fun `should return 10 random meals when potato meals are more than 10`() {
        // Given
        val potatoMeals = (1..20).map { createPotatoMeal(it) }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result)
        assertThat(potatoMeals.containsAll(result)).isTrue()
    }

    @Test
    fun `should handle meals with null ingredients`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal("Null Meal", null),
            createPotatoMeal(1)
        )

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result)
        assertThat(result.first().name).isEqualTo("Potato Meal 1")
    }

    @Test
    fun `should return different results on multiple calls`() {
        // Given
        val potatoMeals = (1..20).map { createPotatoMeal(it) }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result1 = getRandomPotatoMealsUseCase()
        val result2 = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result1).isNotEqualTo(result2)
    }
}