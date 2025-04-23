package logic.usecase


import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetHighCalorieMealUseCase
import org.example.logic.usecase.exceptions.EmptyRandomMealException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date
import kotlin.test.assertEquals


class GetHighCalorieMealUseCaseTest {

    private lateinit var getHighCalorieMealUseCase: GetHighCalorieMealUseCase
    private val mealsRepository: MealsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getHighCalorieMealUseCase = GetHighCalorieMealUseCase(mealsRepository)
    }

    @Test
    fun `getRandomHighCalorieMeal should throw EmptyRandomMealException when name of meal is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(
                name = null,
                description = "The meal with no description",
                calorie = 0.0f
            )
        )
        // when & then
        assertThrows<EmptyRandomMealException> { getHighCalorieMealUseCase.getRandomHighCalorieMeal() }.apply {
            assertThat(message).isEqualTo("the meal contain more than 700 calorie is not found in data")
        }
    }

    @Test
    fun `getRandomHighCalorieMeal should throw EmptyRandomMealException when description of meal is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(name = "chicken", description = null, calorie = 750f),
        )
        // when & then
        assertThrows<EmptyRandomMealException> { getHighCalorieMealUseCase.getRandomHighCalorieMeal() }
    }

    @Test
    fun `getRandomHighCalorieMeal should throw EmptyRandomMealException when calories of meal is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(name = "chicken", description = null, calorie = null),
        )

        // when & then
        assertThrows<EmptyRandomMealException> { getHighCalorieMealUseCase.getRandomHighCalorieMeal() }
    }

    @Test
    fun `getRandomHighCalorieMeal should throw EmptyRandomMealException when the list of meals contain meal with more than 700 calorie`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "chicken",
                description = "the good meal contain just chicken",
                calorie = 600f
            ),
            createMeal(
                name = "chicken",
                description = "the good meal contain just chicken",
                calorie = 700f
            ),
            createMeal(
                name = "chicken with more calorie",
                description = "the good meal contain just chicken",
                calorie = 500f
            )
        )
        // when & then
        assertThrows<EmptyRandomMealException> { getHighCalorieMealUseCase.getRandomHighCalorieMeal() }
    }


    @Test
    fun `getRandomHighCalorieMeal should return correct meal when the meal is valid`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "chicken",
                description = "the good meal contain just chicken",
                calorie = 600f
            ),
            createMeal(
                name = "Hot Dog",
                description = "the good meal contain just chicken",
                calorie = 700f
            ),
            createMeal(
                name = "chicken with more calorie",
                description = "the good meal contain just chicken",
                calorie = 800f
            )
        )
        // when
        val result = getHighCalorieMealUseCase.getRandomHighCalorieMeal()
        val expected = createMeal("chicken with more calorie", "the good meal contain just chicken", 800f)

        // then
        // assertEquals(result,expected)
        assertEquals(result.toString(), expected.toString())
    }



}


fun createMeal(name: String?, description: String?, calorie: Float?): Meal {
    return Meal(
        name = name,
        id = null,
        minutes = null,
        contributorId = null,
        submitted = Date(),
        tags = null,
        nutrition = Nutrition(
            calories = calorie,
            totalFat = null,
            sugar = null,
            sodium = null,
            protein = null,
            saturatedFat = null,
            carbohydrates = null
        ),
        numberOfSteps = null,
        steps = null,
        description = description,
        ingredients = null,
        numberOfIngredients = null
    )
}

