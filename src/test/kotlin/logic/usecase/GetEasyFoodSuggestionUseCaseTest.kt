package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.example.data.CsvMealsRepository
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetEasyFoodSuggestionUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Date


class GetEasyFoodSuggestionUseCaseTest {
    private lateinit var mealsRepository: CsvMealsRepository
    private lateinit var getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase

    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        getEasyFoodSuggestionUseCase = GetEasyFoodSuggestionUseCase(mealsRepository)
    }

    //region getEasyMeals test cases
    @Test
    fun ` getEasyMeals should return list of easy meals having multiple meals vary in easy meal standards`() {
        //Given
        val mixedMeals = mixedMeals
        every { mealsRepository.getAllMeals() } returns mixedMeals.flatten()
        //when
        val result = getEasyFoodSuggestionUseCase.invoke()

        assertThat(result).containsExactlyElementsIn(easyMeals)

    }

    @Test
    fun ` getEasyMeals should Throw NoMealFoundException having no easy meals found`() {
        //Given
        every { mealsRepository.getAllMeals() } returns hardMeals

        //when
        assertThrows<NoMealFoundException> {
            getEasyFoodSuggestionUseCase.invoke()
        }

    }

    @Test
    fun `getEasyMeals should throw NoMealFoundException when meal has too many steps`() {
        every { mealsRepository.getAllMeals() } returns listOf(
            createMealTooManySteps()
        )

        assertThrows<NoMealFoundException> {
            getEasyFoodSuggestionUseCase.invoke()
        }
    }

    @Test
    fun `getEasyMeals should throw NoMealFoundException when meal takes too long to make`() {
        every { mealsRepository.getAllMeals() } returns listOf(
            createMealTooLong()
        )

        assertThrows<NoMealFoundException> {
            getEasyFoodSuggestionUseCase.invoke()
        }
    }

    @Test
    fun `getEasyMeals should throw NoMealFoundException when meal has too many ingredients`() {
        every { mealsRepository.getAllMeals() } returns listOf(
            createMealTooManyIngredients()
        )

        assertThrows<NoMealFoundException> {
            getEasyFoodSuggestionUseCase.invoke()
        }
    }

    @Test
    fun `getEasyMeals should include meals exactly at thresholds (30min, 6 steps, 5 ingredients)`() {
        val edgeCaseMeal = Meal(
            "Edge Case Salad",
            999,
            30,
            8888,
            Date.valueOf("2024-04-01"),
            listOf("healthy", "simple"),
            Nutrition(150f, 10f, 20f, 1f, 0.5f, 2f, 2f),
            6,
            List(6) { "Step $it" },
            "Meets all easy boundaries",
            listOf("lettuce", "tomato", "olive", "feta", "cucumber"),
            5
        )
        every { mealsRepository.getAllMeals() } returns listOf(edgeCaseMeal)

        val result = getEasyFoodSuggestionUseCase.invoke()

        assertEquals(listOf(edgeCaseMeal), result)
    }

    @Test
    fun `getEasyMeals should return meals 10 randomly from more that 10 meals given`() {
        val edgeCaseMeal = listOf(
            createEasyMeal().copy("2"),
            createEasyMeal().copy("3"),
            createEasyMeal().copy("4"),
            createEasyMeal().copy("5"),
            createEasyMeal().copy("6"),
            createEasyMeal().copy("7"),
            createEasyMeal().copy("8"),
            createEasyMeal().copy("9"),
            createEasyMeal().copy("10"),
            createEasyMeal().copy("11"),
            createEasyMeal().copy("12"),
        )
        every { mealsRepository.getAllMeals() } returns edgeCaseMeal
        val result = getEasyFoodSuggestionUseCase.invoke()

        assertEquals(10, result.size)

        assertEquals(10, result.toSet().size)
    }

    // region helper factories

    private fun createEasyMeal() = Meal(
        "Easy Egg Toast",
        1,
        5,
        1000,
        Date.valueOf("2024-02-01"),
        listOf("quick"),
        Nutrition(200f, 10f, 15f, 1f, 1f, 2f, 3f),
        3,
        listOf("Toast bread", "Cook egg", "Assemble"),
        "Simple toast with egg",
        listOf("bread", "egg", "butter", "salt", "pepper"),
        5
    )


    private fun createHardMeal() = Meal(
        "Heavy Lasagna",
        2,
        60,
        101,
        Date.valueOf("2024-01-01"),
        listOf("dinner"),
        Nutrition(800f, 20f, 60f, 10f, 3f, 5f, 7f),
        10,
        List(10) { "step $it" },
        "Complex lasagna",
        listOf("pasta", "cheese", "meat", "sauce", "onion", "garlic", "oil"),
        7
    )

    private fun createMealTooManySteps() = createEasyMeal().copy(
        id = 3,
        steps = List(7) { "Step $it" },
        numberOfSteps = 7
    )

    private fun createMealTooLong() = createEasyMeal().copy(
        id = 4,
        minutes = 45
    )

    private fun createMealTooManyIngredients() = createEasyMeal().copy(
        id = 5,
        ingredients = listOf("a", "b", "c", "d", "e", "f"),
        numberOfIngredients = 6
    )


    private var easyMeals = listOf(
        createEasyMeal(),
        createEasyMeal(),
    )
    private var hardMeals = listOf(
        createHardMeal(),
        createHardMeal(),
    )
    private var mixedMeals = listOf(
        easyMeals,
        hardMeals
    )
    // endregion
}

