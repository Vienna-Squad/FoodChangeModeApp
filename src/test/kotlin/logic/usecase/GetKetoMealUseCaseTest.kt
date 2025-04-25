package logic.usecase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetKetoMealUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date

class GetKetoMealUseCaseTest {

    private lateinit var getKetoMealUseCase: GetKetoMealUseCase
    private val mealsRepository: MealsRepository = mockk()

    @BeforeEach
    fun setUp() {
        getKetoMealUseCase = GetKetoMealUseCase(mealsRepository)
    }

    @Test
    fun `should throw exception when no keto meals available`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(totalFat = 10f, carbs = 50f),
            createMeal(totalFat = 15f, carbs = 30f)
        )

        // When & Then
        assertThrows<NoMealFoundException> {
            getKetoMealUseCase(emptySet())
        }
    }

    @Test
    fun `should return keto meal when meals available`() {
        // Given
        val ketoMeal = createMeal(totalFat = 25f, carbs = 10f)
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(totalFat = 10f, carbs = 50f),
            ketoMeal,
            createMeal(totalFat = 12f, carbs = 45f)
        )

        // When
        val result = getKetoMealUseCase(emptySet())

        // Then
        assertThat(result).isEqualTo(ketoMeal)
    }

    @Test
    fun ` should return unseen KetoMeals when seen meals exist `() {
        // Given
        val seenKetoMeal = createMeal(totalFat = 25f, carbs = 10f)
        val newKetoMeal = createMeal(totalFat = 22f, carbs = 12f)
        every { mealsRepository.getAllMeals() } returns listOf(
            seenKetoMeal,
            newKetoMeal,
            createMeal(totalFat = 10f, carbs = 50f)
        )

        // When
        val result = getKetoMealUseCase(setOf(seenKetoMeal))

        // Then
        assertThat(result).isEqualTo(newKetoMeal)
    }

    @Test
    fun ` should throw exception when nutrition data is missing`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(nutrition = null),
            createMeal(nutrition = null)
        )

        // When & Then
        assertThrows<NoMealFoundException> {
            getKetoMealUseCase(emptySet())
        }
    }

    @Test
    fun `should throw exception when partial nutrition data`() {
        // Given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(totalFat = 25f, carbs = null),
            createMeal(totalFat = null, carbs = 10f)
        )

        // When & Then
        assertThrows<NoMealFoundException> {
            getKetoMealUseCase(emptySet())
        }
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