package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetEggFreeSweetsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date

class GetEggFreeSweetsUseCaseTest {

    private lateinit var getEggFreeSweetsUseCase: GetEggFreeSweetsUseCase
    private val mealsRepository: MealsRepository = mockk()

    @BeforeEach
    fun setUp() {
        getEggFreeSweetsUseCase = GetEggFreeSweetsUseCase(mealsRepository)
    }

    @Test
    fun `should throw exception when no egg-free sweets available`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(ingredients = listOf("Egg", "Flour", "Sugar")),
            createMeal(ingredients = listOf("Eggplant", "Sugar")),
            createMeal(ingredients = listOf("Flour", "Butter"))
        )

        // When & Then
        assertThrows<NoMealFoundException> {
            getEggFreeSweetsUseCase(mutableSetOf())
        }
    }

    @Test
    fun `should return egg-free sweet when available`() {
        // Given
        val eggFreeSweetMeal = createMeal(
            ingredients = listOf("Sugar", "Flour", "Butter"),
            tags = listOf("Dessert")
        )
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(ingredients = listOf("Egg", "Sugar", "Milk")),
            eggFreeSweetMeal,
            createMeal(ingredients = listOf("Flour", "Egg"))
        )

        // When
        val result = getEggFreeSweetsUseCase(mutableSetOf())

        // Then
        assertThat(result).isEqualTo(eggFreeSweetMeal)
    }

    @Test
    fun `should return unseen egg-free sweet when seen meals exist`() {
        // Given
        val seenMeal = createMeal(
            ingredients = listOf("Sugar", "Butter"),
            tags = listOf("Dessert")
        )
        val newMeal = createMeal(
            ingredients = listOf("Sugar", "Chocolate"),
            tags = listOf("Dessert")
        )
        every { mealsRepository.getAllMeals() } returns listOf(
            seenMeal,
            newMeal
        )

        // When
        val result = getEggFreeSweetsUseCase(mutableSetOf(seenMeal)
        )

        // Then
        assertThat(result).isEqualTo(newMeal)
    }

    @Test
    fun `should throw exception when ingredients or tags are missing`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(ingredients = null, tags = listOf("Dessert")),
            createMeal(ingredients = listOf("Sugar", "Flour"), tags = null)
        )

        // When & Then
        assertThrows<NoMealFoundException> {
            getEggFreeSweetsUseCase(mutableSetOf())
        }
    }

    private fun createMeal(
        name: String? = null,
        ingredients: List<String>? = null,
        tags: List<String>? = null
    ): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = tags,
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = ingredients,
            numberOfIngredients = ingredients?.size
        )
    }
}