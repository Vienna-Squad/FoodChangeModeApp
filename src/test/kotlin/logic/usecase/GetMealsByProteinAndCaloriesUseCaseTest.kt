package logic.usecase


import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetMealsByProteinAndCaloriesUseCase
import org.example.logic.usecase.exceptions.NoMatchingMealsFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetMealsByProteinAndCaloriesUseCaseTest {

    private val mockRepository = mockk<MealsRepository>()
    private val useCase = GetMealsByProteinAndCaloriesUseCase(mockRepository)

    @Test
    fun `invoke should return matching meals within tolerance`() {
        val meals = listOf(
            createMeal("Perfect Match", 500f, 30f),
            createMeal("Close Calories", 520f, 30f),
            createMeal("Close Protein", 500f, 32f),
            createMeal("Both Close", 520f, 32f),
            createMeal("Too Far Calories", 600f, 30f),
            createMeal("Too Far Protein", 500f, 40f),
            createMeal("No Nutrition", null, null)
        )

        every { mockRepository.getAllMeals() } returns meals

        val result = useCase(500f, 30f)

        assertThat(result).hasSize(4)
        assertThat(result.map { it.name }).containsExactly(
            "Perfect Match",
            "Close Calories",
            "Close Protein",
            "Both Close"
        )
    }

    @Test
    fun `invoke should throw when no meals have nutrition data`() {
        val meals = listOf(
            createMeal("No Nutrition 1", null, null),
            createMeal("No Nutrition 2", null, null)
        )

        every { mockRepository.getAllMeals() } returns meals

        assertThrows<NoMatchingMealsFoundException> {
            useCase(500f, 30f)
        }
    }

    @Test
    fun `invoke should throw when no meals match criteria`() {
        val meals = listOf(
            createMeal("Too High", 600f, 40f),
            createMeal("Too Low", 400f, 20f)
        )

        every { mockRepository.getAllMeals() } returns meals

        assertThrows<NoMatchingMealsFoundException> {
            useCase(500f, 30f)
        }
    }

    @Test
    fun `invoke should throw when repository is empty`() {
        every { mockRepository.getAllMeals() } returns emptyList()

        assertThrows<NoMatchingMealsFoundException> {
            useCase(500f, 30f)
        }
    }

    @ParameterizedTest
    @ValueSource(floats = [0f, -100f])
    fun `invoke should throw for invalid calories input`(invalidCalories: Float) {
        assertThrows<IllegalArgumentException> {
            useCase(invalidCalories, 30f)
        }
    }

    @ParameterizedTest
    @ValueSource(floats = [0f, -10f])
    fun `invoke should throw for invalid protein input`(invalidProtein: Float) {
        assertThrows<IllegalArgumentException> {
            useCase(500f, invalidProtein)
        }
    }

    private fun createMeal(name: String, calories: Float?, protein: Float?): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = java.util.Date(),
            tags = null,
            nutrition = if (calories != null || protein != null) {
                Nutrition(
                    calories = calories,
                    totalFat = null,
                    sugar = null,
                    sodium = null,
                    protein = protein,
                    saturatedFat = null,
                    carbohydrates = null
                )
            } else {
                null
            },
            numberOfSteps = null,
            steps = null,
            description = null,
            ingredients = null,
            numberOfIngredients = null
        )
    }
}