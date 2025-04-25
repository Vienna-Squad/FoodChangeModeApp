package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetMealsOfCountryUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.KMPSearcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetMealsOfCountryUseCaseTest {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var kmpSearcher: KMPSearcher
    private lateinit var getMealsOfCountryUseCase: GetMealsOfCountryUseCase


    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        kmpSearcher = mockk(relaxed = true)
        getMealsOfCountryUseCase = GetMealsOfCountryUseCase(mealsRepository, kmpSearcher)
    }


    @Test
    fun `should throw NoMealFoundException when country input is blank`() {
        // When & Then
        assertThrows<NoMealFoundException> {
            getMealsOfCountryUseCase("   ")
        }
    }

    @Test
    fun `should return meal when country matches in tags`() {
        // Given
        val meal = createMeal(
            name = "Koshari",
            tags = listOf("egyptian", "street-food"),
            description = "A traditional street food"
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)
        every { kmpSearcher.search("egyptian", "egypt") } returns true
        every { kmpSearcher.search("street-food", "egypt") } returns false
        every { kmpSearcher.search("a traditional street food", "egypt") } returns false

        // When
        val result = getMealsOfCountryUseCase("egypt")

        // Then
        assertThat(result).containsExactly(meal)
    }

    @Test
    fun `should return meal when country matches in description`() {
        // Given
        val meal = createMeal(
            name = "Koshari",
            tags = listOf("street-food"),
            description = "A traditional Egyptian street food"
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)
        every { kmpSearcher.search("street-food", "egypt") } returns false
        every { kmpSearcher.search("a traditional egyptian street food", "egypt") } returns true

        // When
        val result = getMealsOfCountryUseCase("egypt")

        // Then
        assertThat(result).containsExactly(meal)
    }

    @Test
    fun `should return up to 20 meals randomly when more than 20 meals match`() {
        // Given
        val meals = (1..30).map { id ->
            createMeal(
                name = "Koshari $id",
                id = id.toLong(),
                tags = listOf("egyptian"),
                description = "A traditional Egyptian street food",
            )
        }
        every { mealsRepository.getAllMeals() } returns meals
        every { kmpSearcher.search("egyptian", "egypt") } returns true
        every { kmpSearcher.search("A traditional Egyptian street food", "egypt") } returns true

        // When
        val result = getMealsOfCountryUseCase("egypt")

        // Then
        assertThat(result).hasSize(20)
    }

    @Test
    fun `should return all meals when fewer than 20 meals match`() {
        // Given
        val meal = createMeal(
            name = "Koshari",
            tags = listOf("egyptian", "street-food"),
            description = "A traditional street food"
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)
        every { kmpSearcher.search("egyptian", "egypt") } returns true
        every { kmpSearcher.search("A traditional Egyptian street food", "egypt") } returns true

        // When
        val result = getMealsOfCountryUseCase("egypt")

        // Then
        assertThat(result).containsExactly(meal)
    }

    @Test
    fun `should throw NoMealFoundException when country does not match in tags or description `() {
        // Given
        val meal = createMeal(
            name = "Sushi",
            tags = listOf("japanese"),
            description = "A Japanese dish",
            ingredients = listOf("rice", "fish")
        )

        every { mealsRepository.getAllMeals() } returns listOf(meal)
        every { kmpSearcher.search("japanese", "egypt") } returns false
        every { kmpSearcher.search("a japanese dish", "egypt") } returns false

        // When & Then
       assertThrows<NoMealFoundException> {
            getMealsOfCountryUseCase("egypt")
        }
    }

    @Test
    fun `should return meal when tags are null but country matches in description`() {
        // Given
        val meal = createMeal(
            name = "Koshari",
            tags = null,
            description = "A traditional Egyptian street food"
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)
        every { kmpSearcher.search("a traditional egyptian street food", "egypt") } returns true

        // When
        val result = getMealsOfCountryUseCase("egypt")

        // Then
        assertThat(result).containsExactly(meal)
    }

    @Test
    fun `should return meal when description is null but country matches in tags`() {
        // Given
        val meal = createMeal(
            name = "Koshari",
            tags = listOf("egyptian", "street-food"),
            description = null
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)
        every { kmpSearcher.search("egyptian", "egypt") } returns true
        every { kmpSearcher.search("street-food", "egypt") } returns false

        // When
        val result = getMealsOfCountryUseCase("egypt")

        // Then
        assertThat(result).containsExactly(meal)
    }



    private fun createMeal(
        name: String,
        id: Long = 1L,
        tags: List<String>? = null,
        description: String? = null,
        minutes: Long = 45L,
        contributorId: Long = 101L,
        nutrition: Nutrition = Nutrition(
            calories = 700f,
            totalFat = 15f,
            sugar = 5f,
            sodium = 300f,
            protein = 20f,
            saturatedFat = 3f,
            carbohydrates = 120f
        ),
        numberOfSteps: Int = 5,
        steps: List<String> = listOf("Step 1", "Step 2"),
        ingredients: List<String> = listOf("ingredient1", "ingredient2"),
        numberOfIngredients: Int = 2
    ): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = minutes,
            contributorId = contributorId,
            submitted = Date(),
            tags = tags,
            nutrition = nutrition,
            numberOfSteps = numberOfSteps,
            steps = steps,
            description = description,
            ingredients = ingredients,
            numberOfIngredients = numberOfIngredients
        )
    }
}