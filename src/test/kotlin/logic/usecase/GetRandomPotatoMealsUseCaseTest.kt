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
    private val mealsRepository: MealsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getRandomPotatoMealsUseCase = GetRandomPotatoMealsUseCase(mealsRepository)
    }

    @Test
    fun `invoke should return empty list when no meal contains potato`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal("Salad", listOf("lettuce", "tomato")),
            createMeal("Beef", listOf("beef", "onion"))
        )

        // When
        val result = getRandomPotatoMealsUseCase.invoke()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `invoke should return all meals when potato meals are less than or equal to 10`() {
        // Given
        val potatoMeals = (1..5).map {
            createMeal("Potato Meal $it", listOf("potato", "salt"))
        }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result = getRandomPotatoMealsUseCase.invoke()

        // Then
        assertThat(result).hasSize(5)
        assertThat(result).containsExactlyElementsIn(potatoMeals)
    }

    @Test
    fun `invoke should return 10 meals when potato meals are more than 10`() {
        // Given
        val potatoMeals = (1..20).map {
            createMeal("Potato Meal $it", listOf("mashed potato", "butter"))
        }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result = getRandomPotatoMealsUseCase.invoke()

        // Then
        assertThat(result).hasSize(10)
        assertThat(potatoMeals.containsAll(result)).isTrue()    }

    @Test
    fun `invoke should handle meal with null ingredients safely`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal("Null Meal", null),
            createMeal("Potato Soup", listOf("potato", "onion"))
        )

        // When
        val result = getRandomPotatoMealsUseCase.invoke()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result.first().name).isEqualTo("Potato Soup")
    }
    @Test
    fun `invoke should return different meals on subsequent calls`() {
        // Given
        val potatoMeals = (1..20).map {
            createMeal("Potato Meal $it", listOf("mashed potato", "butter"))
        }
        every { mealsRepository.getAllMeals() } returns potatoMeals

        // When
        val result1 = getRandomPotatoMealsUseCase.invoke()
        val result2 = getRandomPotatoMealsUseCase.invoke()

        // Then
        assertThat(result1).hasSize(10)
        assertThat(result2).hasSize(10)
        assertThat(result1).isNotEqualTo(result2)
    }
}

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
