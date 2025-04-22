package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetItalianGroupMealsUseCase
import org.example.logic.usecase.exceptions.NoItalianGroupMealsException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetItalianGroupMealsUseCaseTest {
    private lateinit var mealsRepository: MealsRepository
    private lateinit var getItalianGroupMealsUseCase: GetItalianGroupMealsUseCase

    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        getItalianGroupMealsUseCase = GetItalianGroupMealsUseCase(mealsRepository)
    }

    @Test
    fun `should return meal when it has both italian and for-large-groups tags`() {

        // Given
        val meal = Meal(
            name = "Pizza",
            id = 1L,
            minutes = 45L,
            contributorId = 101L,
            submitted = Date(),
            tags = listOf("italian", "for-large-groups"),
            nutrition = Nutrition(
                calories = 700f,
                totalFat = 15f,
                sugar = 5f,
                sodium = 300f,
                protein = 20f,
                saturatedFat = 3f,
                carbohydrates = 120f
            ),
            numberOfSteps = 5,
            steps = listOf("Step 1", "Step 2"),
            description = "A classic Italian dish",
            ingredients = listOf("dough", "cheese"),
            numberOfIngredients = 2
        )

        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When
        val result = getItalianGroupMealsUseCase.invoke()

        // Then
        assertThat(result).containsExactly(meal)
    }

    @Test
    fun `should throw NoItalianGroupMealsException when meal does not have both required tags`() {
        // Given
        val meal = Meal(
            name = "Sushi",
            id = 1L,
            minutes = 60L,
            contributorId = 101L,
            submitted = Date(),
            tags = listOf("japanese"),
            nutrition = Nutrition(
                calories = 500f,
                totalFat = 10f,
                sugar = 2f,
                sodium = 600f,
                protein = 25f,
                saturatedFat = 2f,
                carbohydrates = 80f
            ),
            numberOfSteps = 6,
            steps = listOf("Step 1", "Step 2"),
            description = "A Japanese dish",
            ingredients = listOf("rice", "fish"),
            numberOfIngredients = 2
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoItalianGroupMealsException> {
            getItalianGroupMealsUseCase.invoke()
        }
    }

    @Test
    fun `should throw NoItalianGroupMealsException when meal has only one of the required tags`() {
        // Given
        val meal = Meal(
            name = "Burger",
            id = 1L,
            minutes = 30L,
            contributorId = 101L,
            submitted = Date(),
            tags = listOf("american", "for-large-groups"),
            nutrition = Nutrition(
                calories = 600f,
                totalFat = 20f,
                sugar = 3f,
                sodium = 800f,
                protein = 30f,
                saturatedFat = 8f,
                carbohydrates = 50f
            ),
            numberOfSteps = 4,
            steps = listOf("Step 1", "Step 2"),
            description = "An American burger",
            ingredients = listOf("bun", "meat"),
            numberOfIngredients = 2
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoItalianGroupMealsException> {
            getItalianGroupMealsUseCase.invoke()
        }
    }

    @Test
    fun `should throw NoItalianGroupMealsException when tags are null`() {
        // Given
        val meal = Meal(
            name = "Sushi",
            id = 1L,
            minutes = 60L,
            contributorId = 101L,
            submitted = Date(),
            tags = null,
            nutrition = Nutrition(
                calories = 500f,
                totalFat = 10f,
                sugar = 2f,
                sodium = 600f,
                protein = 25f,
                saturatedFat = 2f,
                carbohydrates = 80f
            ),
            numberOfSteps = 6,
            steps = listOf("Step 1", "Step 2"),
            description = "A Japanese dish",
            ingredients = listOf("rice", "fish"),
            numberOfIngredients = 2
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoItalianGroupMealsException> {
            getItalianGroupMealsUseCase.invoke()
        }
    }

    @Test
    fun `should throw NoItalianGroupMealsException when tags are empty`() {
        // Given
        val meal = Meal(
            name = "Sushi",
            id = 1L,
            minutes = 60L,
            contributorId = 101L,
            submitted = Date(),
            tags = emptyList(),
            nutrition = Nutrition(
                calories = 500f,
                totalFat = 10f,
                sugar = 2f,
                sodium = 600f,
                protein = 25f,
                saturatedFat = 2f,
                carbohydrates = 80f
            ),
            numberOfSteps = 6,
            steps = listOf("Step 1", "Step 2"),
            description = "A Japanese dish",
            ingredients = listOf("rice", "fish"),
            numberOfIngredients = 2
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When && Then
        assertThrows<NoItalianGroupMealsException> {
            getItalianGroupMealsUseCase.invoke()
        }
    }

    @Test
    fun `should return meal when tags have different case`() {
        // Given
        val meal = Meal(
            name = "Pizza",
            id = 1L,
            minutes = 45L,
            contributorId = 101L,
            submitted = Date(),
            tags = listOf("ITALIAN", "For-Large-Groups"),
            nutrition = Nutrition(
                calories = 700f,
                totalFat = 15f,
                sugar = 5f,
                sodium = 300f,
                protein = 20f,
                saturatedFat = 3f,
                carbohydrates = 120f
            ),
            numberOfSteps = 5,
            steps = listOf("Step 1", "Step 2"),
            description = "A classic Italian dish",
            ingredients = listOf("dough", "cheese"),
            numberOfIngredients = 2
        )
        every { mealsRepository.getAllMeals() } returns listOf(meal)

        // When
        val result = getItalianGroupMealsUseCase.invoke()

        // Then
        assertThat(result).containsExactly(meal)
    }
}