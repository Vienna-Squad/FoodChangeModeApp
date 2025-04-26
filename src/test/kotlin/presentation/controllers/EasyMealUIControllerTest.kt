package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.usecase.GetEasyFoodSuggestionUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.EasyMealUIController
import org.example.utils.viewer.ItemsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Date
import java.util.*

class EasyMealUIControllerTest {

    private lateinit var easyMealUIController: EasyMealUIController
    private lateinit var getEasyFoodSuggestionUseCase: GetEasyFoodSuggestionUseCase
    private lateinit var viewer: ItemsViewer<Meal>

    @BeforeEach
    fun setup() {
        getEasyFoodSuggestionUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        easyMealUIController = EasyMealUIController(getEasyFoodSuggestionUseCase, viewer)
    }

    @Test
    fun `calling execute should call  viewItems function `() {
        //Given
        val fakeMeals= listOf(
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
        every { getEasyFoodSuggestionUseCase.invoke() } returns fakeMeals
        //When
        easyMealUIController.execute()
        //Then
        verify {viewer.viewItems(any()) }
    }
    @Test
    fun `calling execute should throw meal not found exception`() {
        //Given
        every { getEasyFoodSuggestionUseCase.invoke() } returns emptyList()
        //Assertion
        assertThrows<NoMealFoundException>{easyMealUIController.execute() }
    }
}