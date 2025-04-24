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

    /**
     * easy meal is Under 30 minutes preparation time,
     * has 5 ingredients or fewer,
     * and can be prepared in less than 6 steps*/
    //region getEasyMeals test cases
    @Test
    fun ` getEasyMeals should return list of easy meals having multiple meals vary in easy meal standards`() {
        //Given
        every { mealsRepository.getAllMeals() } returns listOf(

            Meal(
                "Avocado Toast Deluxe", // Easy
                1001,
                10,
                501,
                Date.valueOf("2024-11-03"),
                listOf("breakfast", "easy", "vegetarian", "10-minutes-or-less", "snack", "quick", "no-cook"),
                Nutrition(250.0f, 12.0f, 22.0f, 2.0f, 7.0f, 1.0f, 5.0f),
                4,
                listOf(
                    "Toast the bread slices until golden.",
                    "Mash the avocado with salt and lemon juice.",
                    "Spread the avocado on toast.",
                    "Top with sliced cherry tomatoes and a sprinkle of chili flakes."
                ),
                "A quick, healthy, and satisfying breakfast or snack that's ready in minutes.",
                listOf("bread", "avocado", "lemon juice", "cherry tomatoes", "chili flakes"),
                5
            ),
            Meal(
                "Quick Veggie Stir-Fry", // Easy
                1002,
                20,
                6789,
                Date.valueOf("2023-08-10"),
                listOf("quick", "vegetarian", "easy", "30-minutes-or-less", "low-effort", "vegetables", "stir-fry"),
                Nutrition(15.0f, 0.0f, 12.0f, 3.0f, 5.0f, 0.0f, 1.0f),
                5,
                listOf(
                    "Chop vegetables (carrot, bell pepper, broccoli).",
                    "Heat a pan with olive oil.",
                    "Stir-fry the vegetables for 5-7 minutes.",
                    "Add soy sauce and garlic.",
                    "Cook for another 2-3 minutes until tender."
                ),
                "This stir-fry is a quick and healthy dish that’s packed with flavor. It’s a great option for a busy evening.",
                listOf("carrot", "bell pepper", "broccoli", "soy sauce", "garlic"),
                5
            ),
            Meal(
                "Chicken Parmesan",
                1003,
                45,
                502,
                Date.valueOf("2023-02-21"),
                listOf("main-course", "italian", "chicken", "dinner", "comfort-food"),
                Nutrition(400.0f, 20.0f, 30.0f, 8.0f, 15.0f, 2.0f, 10.0f),
                8,
                listOf(
                    "Preheat oven to 375°F.",
                    "Season chicken with salt and pepper, then coat in breadcrumbs.",
                    "Pan-fry chicken until golden and crispy.",
                    "Top with marinara sauce and mozzarella cheese.",
                    "Bake in the oven for 20 minutes until cheese is melted.",
                    "Serve with pasta or a side salad."
                ),
                "A classic Italian dish with breaded chicken topped with marinara and melted cheese.",
                listOf("chicken breast", "breadcrumbs", "mozzarella", "marinara sauce", "parmesan"),
                5
            ),
            Meal(
                "Beef Stew",
                1004,
                90,
                503,
                Date.valueOf("2022-11-14"),
                listOf("main-course", "comfort-food", "beef", "slow-cooked", "hearty"),
                Nutrition(350.0f, 25.0f, 10.0f, 5.0f, 8.0f, 4.0f, 6.0f),
                7,
                listOf(
                    "Brown the beef chunks in a large pot.",
                    "Add chopped onions, carrots, and potatoes.",
                    "Pour in beef broth and season with salt, pepper, and herbs.",
                    "Simmer for 60-90 minutes until the beef is tender.",
                    "Add flour to thicken the stew.",
                    "Serve hot with bread."
                ),
                "A hearty and rich stew that's perfect for cold days. It’s slow-cooked for maximum flavor.",
                listOf("beef", "carrots", "potatoes", "onions", "beef broth"),
                5
            )
        )


        //when
        val result = getEasyFoodSuggestionUseCase.invoke()
        assertThat(result).containsExactlyElementsIn(
            listOf(
                Meal(
                    "Avocado Toast Deluxe", // Easy
                    1001,
                    10,
                    501,
                    Date.valueOf("2024-11-03"),
                    listOf("breakfast", "easy", "vegetarian", "10-minutes-or-less", "snack", "quick", "no-cook"),
                    Nutrition(250.0f, 12.0f, 22.0f, 2.0f, 7.0f, 1.0f, 5.0f),
                    4,
                    listOf(
                        "Toast the bread slices until golden.",
                        "Mash the avocado with salt and lemon juice.",
                        "Spread the avocado on toast.",
                        "Top with sliced cherry tomatoes and a sprinkle of chili flakes."
                    ),
                    "A quick, healthy, and satisfying breakfast or snack that's ready in minutes.",
                    listOf("bread", "avocado", "lemon juice", "cherry tomatoes", "chili flakes"),
                    5
                ),
                Meal(
                    "Quick Veggie Stir-Fry", // Easy
                    1002,
                    20,
                    6789,
                    Date.valueOf("2023-08-10"),
                    listOf("quick", "vegetarian", "easy", "30-minutes-or-less", "low-effort", "vegetables", "stir-fry"),
                    Nutrition(15.0f, 0.0f, 12.0f, 3.0f, 5.0f, 0.0f, 1.0f),
                    5,
                    listOf(
                        "Chop vegetables (carrot, bell pepper, broccoli).",
                        "Heat a pan with olive oil.",
                        "Stir-fry the vegetables for 5-7 minutes.",
                        "Add soy sauce and garlic.",
                        "Cook for another 2-3 minutes until tender."
                    ),
                    "This stir-fry is a quick and healthy dish that’s packed with flavor. It’s a great option for a busy evening.",
                    listOf("carrot", "bell pepper", "broccoli", "soy sauce", "garlic"),
                    5
                )
            )
        )
    }

    @Test
    fun ` getEasyMeals should Throw NoMealFoundException having no easy meals found`() {
        //Given
        every { mealsRepository.getAllMeals() } returns listOf(
            Meal(
                "Chicken Parmesan",
                1003,
                45,
                502,
                Date.valueOf("2023-02-21"),
                listOf("main-course", "italian", "chicken", "dinner", "comfort-food"),
                Nutrition(400.0f, 20.0f, 30.0f, 8.0f, 15.0f, 2.0f, 10.0f),
                8,
                listOf(
                    "Preheat oven to 375°F.",
                    "Season chicken with salt and pepper, then coat in breadcrumbs.",
                    "Pan-fry chicken until golden and crispy.",
                    "Top with marinara sauce and mozzarella cheese.",
                    "Bake in the oven for 20 minutes until cheese is melted.",
                    "Serve with pasta or a side salad."
                ),
                "A classic Italian dish with breaded chicken topped with marinara and melted cheese.",
                listOf("chicken breast", "breadcrumbs", "mozzarella", "marinara sauce", "parmesan"),
                5
            ),
            Meal(
                "Beef Stew",
                1004,
                90,
                503,
                Date.valueOf("2022-11-14"),
                listOf("main-course", "comfort-food", "beef", "slow-cooked", "hearty"),
                Nutrition(350.0f, 25.0f, 10.0f, 5.0f, 8.0f, 4.0f, 6.0f),
                7,
                listOf(
                    "Brown the beef chunks in a large pot.",
                    "Add chopped onions, carrots, and potatoes.",
                    "Pour in beef broth and season with salt, pepper, and herbs.",
                    "Simmer for 60-90 minutes until the beef is tender.",
                    "Add flour to thicken the stew.",
                    "Serve hot with bread."
                ),
                "A hearty and rich stew that's perfect for cold days. It’s slow-cooked for maximum flavor.",
                listOf("beef", "carrots", "potatoes", "onions", "beef broth"),
                5
            )
        )
        //when
        assertThrows<NoMealFoundException> {
            getEasyFoodSuggestionUseCase.invoke()
        }

    }

    @Test
    fun `getEasyMeals should return only one easy meal among hard meals`() {
        every { mealsRepository.getAllMeals() } returns listOf(
            createHardMeal(),
            createMealTooManySteps(),
            createMealTooLong(),
            createMealTooManyIngredients(),
            createEasyMeal()
        )

        val result = getEasyFoodSuggestionUseCase.invoke()

        assertEquals(listOf(createEasyMeal()), result)
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

    // endregion
}

