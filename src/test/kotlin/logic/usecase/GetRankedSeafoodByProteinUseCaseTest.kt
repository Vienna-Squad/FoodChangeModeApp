package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.model.RankedMealResult
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetRankedSeafoodByProteinUseCase
import org.example.logic.usecase.exceptions.NoSeafoodFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetRankedSeafoodByProteinUseCaseTest {

    private val mockRepository = mockk<MealsRepository>()
    private val useCase = GetRankedSeafoodByProteinUseCase(mockRepository)

    @Test
    fun `invoke should return ranked seafood meals by protein descending`() {
        val meals = listOf(
            createSeafoodMeal("Salmon", 25f),
            createSeafoodMeal("Tuna", 30f),
            createSeafoodMeal("طعمية بالسمك المشوي", 20f),
            createMeal("اي حاجه مفيهاش زفارة", null) // Non-seafood
        )

        every { mockRepository.getAllMeals() } returns meals

        val result = useCase()

        assertThat(result).hasSize(3)
        assertThat(result).containsExactly(
            RankedMealResult(1, "Tuna", 30f),
            RankedMealResult(2, "Salmon", 25f),
            RankedMealResult(3, "طعمية بالسمك المشوي", 20f)
        ).inOrder()
    }

    @Test
    fun `invoke should handle various seafood tags`() {
        val meals = listOf(
            createMeal("Fish Tacos", 22f, listOf("mexican", "fish")),
            createMeal("Seafood Pasta", 18f, listOf("pasta", "seafood")),
            createMeal("Grilled Salmon", 25f, listOf("salmon", "grilled"))
        )

        every { mockRepository.getAllMeals() } returns meals

        val result = useCase()

        assertThat(result).hasSize(3)
        assertThat(result.map { it.name }).containsExactly(
            "Grilled Salmon",
            "Fish Tacos",
            "Seafood Pasta"
        )
    }

    @Test
    fun `invoke should throw when no seafood meals found`() {
        val meals = listOf(
            createMeal("Chicken Curry", 25f, listOf("poultry")),
            createMeal("Beef Steak", 30f, listOf("meat"))
        )

        every { mockRepository.getAllMeals() } returns meals

        assertThrows<NoSeafoodFoundException> {
            useCase()
        }
    }

    @Test
    fun `invoke should throw when no meals with protein data`() {
        val meals = listOf(
            createSeafoodMeal("Mystery Fish", null),
            createSeafoodMeal("Unknown Seafood", null)
        )

        every { mockRepository.getAllMeals() } returns meals

        assertThrows<NoSeafoodFoundException> {
            useCase()
        }
    }

    @Test
    fun `invoke should throw when repository is empty`() {
        every { mockRepository.getAllMeals() } returns emptyList()

        assertThrows<NoSeafoodFoundException> {
            useCase()
        }
    }

    private fun createSeafoodMeal(name: String, protein: Float?): Meal {
        return createMeal(name, protein, listOf("seafood"))
    }

    private fun createMeal(name: String, protein: Float?, tags: List<String>? = null): Meal {
        return Meal(
            name = name,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = java.util.Date(),
            tags = tags,
            nutrition = if (protein != null) {
                Nutrition(
                    calories = null,
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