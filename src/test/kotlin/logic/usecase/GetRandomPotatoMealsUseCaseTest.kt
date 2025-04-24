package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetRandomPotatoMealsUseCase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

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
            createMeal(ingredients = listOf("fish")),
            createMeal(ingredients = listOf("meat")  )
        )

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return all meals when potato meals are 10 `() {
        // Given
        val potatoMeals = (1..10).map { createMeal( ingredients = listOf("potatoes")) }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result).containsExactlyElementsIn(potatoMeals)
    }

    @Test
    fun `should return 10 random meals when potato meals are more than 10`() {
        // Given
        val potatoMeals = (1..20).map { createMeal( ingredients = listOf("potatoes")) }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result.size).isEqualTo(10)
        assertThat(potatoMeals).containsAtLeastElementsIn(result)

    }

    @Test
    fun `should handle meals with null ingredients`() {
        // Given
        val list = listOf(
            createMeal(name = "Potato Meal 1", ingredients = listOf("potatoes")),
            createMeal()
        )
        every { mealsRepository.getAllMeals() } returns list

        // When
         val result = getRandomPotatoMealsUseCase()

        // Then
        assertThat(result.map { it.name }).containsExactly("Potato Meal 1")
    }


    private fun createMeal(
        name: String? = null,
        totalFat: Float? = null,
        carbs: Float? = null,
        nutrition: Nutrition? = Nutrition(
            calories = null,
            totalFat = totalFat,
            sugar = null,
            sodium = null,
            protein = null,
            saturatedFat = null,
            carbohydrates = carbs
        ),
        ingredients: List<String>? = null
    ): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = null,
            nutrition = nutrition,
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = ingredients,
            numberOfIngredients = ingredients?.size
        )
    }
}