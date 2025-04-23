package logic.usecase

import io.mockk.every
import io.mockk.mockk
import org.example.data.CsvMealsRepository
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetEasyFoodSuggestionUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import com.google.common.truth.Truth.assertThat
import io.mockk.verify
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.sql.Date
import kotlin.test.assertTrue
import kotlin.test.assertFalse

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
        assertThrows<NoMealFoundException>{
            getEasyFoodSuggestionUseCase.invoke()
        }

    }
//endregion

    @Test
    fun `getEasFoodSuggestion should call getAllMealsFunction`() {
        //when
        getEasyFoodSuggestionUseCase.invoke()

        //then
        verify { mealsRepository.getAllMeals() }
    }

    //region isEasyMeal test cases

    @Test
    fun `isEasyMeal should return true when meal is an easy meal`() {
        //Given
        //region fakeMeal
        val fakeMeal =
            Meal(
                "Grilled Cheese Sandwich",
                101,
                10,
                5001,
                Date.valueOf("2024-03-10"),
                listOf("easy", "quick", "snack", "cheese", "vegetarian"),
                Nutrition(300f, 15f, 25f, 2f, 1f, 5f, 10f),
                4,
                listOf(
                    "Butter the bread slices",
                    "Place cheese between slices",
                    "Grill on skillet until golden",
                    "Serve hot"
                ),
                "A quick and satisfying cheesy snack.",
                listOf("bread", "cheese", "butter", "salt", "pepper"),
                5
            )
        //endregion

        //when

        val result = getEasyFoodSuggestionUseCase.isEasyMeal(fakeMeal)
        assertTrue { result }
    }

    @Test
    fun `isEasyMeal should return false when meal is not an easy meal (all conditions fail)`() {
        val hardMeal = Meal(
            "Big Lasagna",
            2,
            60,
            101,
            Date.valueOf("2024-01-01"),
            listOf("dinner"),
            Nutrition(800f, 20f, 60f, 10f, 3f, 5f, 7f),
            10,
            List(10) { "step $it" },
            "A complex lasagna.",
            listOf("pasta", "cheese", "meat", "sauce", "onion", "garlic", "oil"),
            7
        )

        val result = getEasyFoodSuggestionUseCase.isEasyMeal(hardMeal)
        assertFalse(result)
    }

    @Test
    fun `isEasyMeal should return false when only ingredients condition fails`() {
        val partialHardMeal = Meal(
            "Salad with extras",
            3,
            15,
            102,
            Date.valueOf("2024-01-01"),
            listOf("light", "healthy"),
            Nutrition(200f, 5f, 10f, 2f, 1f, 2f, 1f),
            4,
            listOf("Chop veggies", "Mix", "Add dressing", "Serve"),
            "Slightly complex salad.",
            listOf("lettuce", "tomato", "cucumber", "feta", "olive", "dressing"),
            6 // too many ingredients
        )

        val result = getEasyFoodSuggestionUseCase.isEasyMeal(partialHardMeal)
        assertFalse(result)
    }

    @Test
    fun `isEasyMeal should return false when only minutes condition fails`() {
        val mealWithTooMuchTime = Meal(
            "Slow Cooked Meal",
            5,
            45,
            104,
            Date.valueOf("2024-01-01"),
            listOf("slow"),
            Nutrition(350f, 10f, 30f, 5f, 2f, 3f, 5f),
            4,
            listOf("Step 1", "Step 2", "Step 3", "Step 4"),
            "Takes too long.",
            listOf("rice", "chicken", "spices"),
            3
        )

        val result = getEasyFoodSuggestionUseCase.isEasyMeal(mealWithTooMuchTime)
        assertFalse(result)
    }

    @Test
    fun `isEasyMeal should return false when only steps condition fails`() {
        val mealWithTooManySteps = Meal(
            "Multi-step Omelette",
            6,
            15,
            105,
            Date.valueOf("2024-01-01"),
            listOf("breakfast"),
            Nutrition(250f, 8f, 20f, 2f, 1f, 2f, 3f),
            7, // fails steps > 6
            List(7) { "Step ${it + 1}" },
            "Too many steps for an omelette.",
            listOf("egg", "cheese", "milk"),
            3
        )

        val result = getEasyFoodSuggestionUseCase.isEasyMeal(mealWithTooManySteps)
        assertFalse(result)
    }
//endregion

    //region isLessThanSevenStepsToPrepare test cases
    @ParameterizedTest
    @ValueSource(longs = [5,6])
    fun `hasSixOrFewerStepsToPrepare Should return true when number of steps is equal to or less than 6`() {
        //Given
        val numberOfSteps = 6

        //then
        val result = getEasyFoodSuggestionUseCase.hasSixOrFewerStepsToPrepare(numberOfSteps)
        assertTrue { result }
    }

    @Test
    fun `hasSixOrFewerStepsToPrepare Should return false when number of steps is more than six`() {
        //Given
        val numberOfSteps = 7

        //then
        val result = getEasyFoodSuggestionUseCase.hasSixOrFewerStepsToPrepare(numberOfSteps)
        assertFalse { result }
    }

    //endregion

    //region isLessThanThirtyMinutesToMake test cases

    @ParameterizedTest
    @ValueSource(longs = [15,30])
    fun `isLessThanThirtyMinutesToMake should return true when time is less than or equal 30 minutes`(minutes: Long) {
        // When
        val result = getEasyFoodSuggestionUseCase.isLessThanThirtyMinutesToMake(minutes)
        // Then
        assertTrue(result)
    }

    @Test
    fun `isLessThanThirtyMinutesToMake should return false when time is more than 30 minutes `() {
        // Given
        val minutes = 35L

        // When
        val result = getEasyFoodSuggestionUseCase.isLessThanThirtyMinutesToMake(minutes)

        // Then
        assertFalse(result)
    }
    //endregion

    //region isLessThanThirtyMinutesToMake test cases
    @ParameterizedTest
    @ValueSource(longs = [3,5])
    fun `isLessThanSixIngredients should return true when ingredients count is equal to or less than 5`() {
        // Given
        val count = 4

        // When
        val result = getEasyFoodSuggestionUseCase.isLessThanSixIngredients(count)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isLessThanSixIngredients should return false when ingredients count is 6 or more`() {
        // Given
        val count = 6

        // When
        val result = getEasyFoodSuggestionUseCase.isLessThanSixIngredients(count)

        // Then
        assertFalse(result)
    }
    //endregion


}