package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetIraqiMealsUseCase
import org.example.logic.usecase.exceptions.NoMealsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


class GetIraqiMealsUseCaseTest {
    lateinit var getIraqiMealsUseCase: GetIraqiMealsUseCase
    val mealsRepository: MealsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        getIraqiMealsUseCase = GetIraqiMealsUseCase(mealsRepository)
    }

    @Test
    fun `when invoke getIraqiMealsUseCase should return just iraqi meals`() {
        //given
        every { mealsRepository.getAllMeals() } returns listOf(
            Meal(
                name = "Masgouf",
                id = 1L,
                minutes = 90L,
                contributorId = 101L,
                submitted = Date(),
                tags = listOf("Grilled", "Fish", "Traditional", "Iraqi"),
                nutrition = Nutrition(320f, 15f, 1.5f, 220f, 35f, 4.5f, 8f),
                numberOfSteps = 5,
                steps = listOf(
                    "Clean and butterfly the fish",
                    "Season with tamarind, lemon, and spices",
                    "Grill over open flame until crispy",
                    "Serve with pickles and bread",
                    "Garnish with lemon slices"
                ),
                description = "Iraq’s national dish – grilled carp seasoned with tamarind and spices.",
                ingredients = listOf("Carp", "Tamarind", "Salt", "Black Pepper", "Lemon"),
                numberOfIngredients = 5
            ),
            Meal(
                name = "Spaghetti Carbonara",
                id = 2L,
                minutes = 30L,
                contributorId = 201L,
                submitted = Date(),
                tags = listOf("Italian", "Pasta", "Quick"),
                nutrition = Nutrition(410f, 18f, 2f, 300f, 17f, 6f, 45f),
                numberOfSteps = 4,
                steps = listOf(
                    "Cook spaghetti until al dente",
                    "Fry pancetta until crispy",
                    "Mix eggs and cheese in a bowl",
                    "Combine all and toss quickly"
                ),
                description = "A classic creamy pasta dish from Italy with eggs, cheese, and pancetta.",
                ingredients = listOf("Spaghetti", "Eggs", "Pancetta", "Parmesan", "Black Pepper"),
                numberOfIngredients = 5
            ),
            Meal(
                name = "Dolma",
                id = 3L,
                minutes = 120L,
                contributorId = 102L,
                submitted = Date(),
                tags = listOf("Stuffed", "Vegetables", "Grape Leaves", "Iraqi"),
                nutrition = Nutrition(380f, 20f, 4f, 330f, 14f, 5f, 38f),
                numberOfSteps = 6,
                steps = listOf(
                    "Prepare stuffing with rice, meat, herbs",
                    "Core and prepare vegetables",
                    "Stuff vegetables and roll grape leaves",
                    "Layer in pot and cover with tomato broth",
                    "Simmer for 1.5 hours",
                    "Let rest before serving"
                ),
                description = "Slow-cooked stuffed vegetables and grape leaves in tomato sauce.",
                ingredients = listOf("Rice", "Minced meat", "Tomato", "Onion", "Grape leaves", "Zucchini", "Eggplant"),
                numberOfIngredients = 7
            ),
            Meal(
                name = "Sushi Roll",
                id = 4L,
                minutes = 45L,
                contributorId = 202L,
                submitted = Date(),
                tags = listOf("Japanese", "Seafood", "Cold"),
                nutrition = Nutrition(250f, 6f, 3f, 400f, 12f, 1f, 35f),
                numberOfSteps = 5,
                steps = listOf(
                    "Cook sushi rice and season it",
                    "Lay nori and rice on bamboo mat",
                    "Add fillings like fish and vegetables",
                    "Roll tightly and slice",
                    "Serve with soy sauce and wasabi"
                ),
                description = "A Japanese delicacy made of vinegared rice and seafood or vegetables.",
                ingredients = listOf("Sushi rice", "Nori", "Raw fish", "Cucumber", "Soy sauce"),
                numberOfIngredients = 5
            ),
            Meal(
                name = "Kubba Mosul",
                id = 5L,
                minutes = 75L,
                contributorId = 103L,
                submitted = Date(),
                tags = listOf("Fried", "Stuffed", "Bulgar", "Iraqi"),
                nutrition = Nutrition(450f, 25f, 1f, 290f, 20f, 6f, 35f),
                numberOfSteps = 4,
                steps = listOf(
                    "Prepare bulgur dough",
                    "Make meat filling with spices",
                    "Shape into flat discs with filling inside",
                    "Deep fry until golden"
                ),
                description = "Crispy flat discs of bulgur stuffed with seasoned minced meat.",
                ingredients = listOf("Bulgur", "Minced meat", "Onions", "Spices", "Oil"),
                numberOfIngredients = 5
            ),
            Meal(
                name = "Chicken Biryani",
                id = 6L,
                minutes = 90L,
                contributorId = 203L,
                submitted = Date(),
                tags = listOf("Spiced", "Rice", "Indian"),
                nutrition = Nutrition(520f, 22f, 5f, 360f, 25f, 6f, 50f),
                numberOfSteps = 6,
                steps = listOf(
                    "Marinate chicken with spices",
                    "Cook rice with saffron and spices",
                    "Layer chicken and rice",
                    "Cook over low heat (dum)",
                    "Garnish with coriander",
                    "Serve hot with yogurt"
                ),
                description = "Aromatic rice dish with spiced chicken, saffron, and herbs.",
                ingredients = listOf("Chicken", "Basmati Rice", "Yogurt", "Spices", "Onions", "Garlic"),
                numberOfIngredients = 6
            )
        )
        //when && then
        assertThat(getIraqiMealsUseCase().size).isEqualTo(3)
    }

    @Test
    fun `when invoke getIraqiMealsUseCase and return empty lis of meals should throw NoMealsFoundException`() {
        //given
        every { mealsRepository.getAllMeals() } returns emptyList()
        //when && then
        assertThrows<NoMealsFoundException> {
            getIraqiMealsUseCase()
        }
    }

    @Test
    fun `when invoke getIraqiMealsUseCase and return lis of non-iraqi meals should throw NoMealsFoundException`() {
        //given
        every { mealsRepository.getAllMeals() } returns listOf(
            Meal(
                name = "Spaghetti Carbonara",
                id = 2L,
                minutes = 30L,
                contributorId = 201L,
                submitted = Date(),
                tags = listOf("Italian", "Pasta", "Quick"),
                nutrition = Nutrition(410f, 18f, 2f, 300f, 17f, 6f, 45f),
                numberOfSteps = 4,
                steps = listOf(
                    "Cook spaghetti until al dente",
                    "Fry pancetta until crispy",
                    "Mix eggs and cheese in a bowl",
                    "Combine all and toss quickly"
                ),
                description = "A classic creamy pasta dish from Italy with eggs, cheese, and pancetta.",
                ingredients = listOf("Spaghetti", "Eggs", "Pancetta", "Parmesan", "Black Pepper"),
                numberOfIngredients = 5
            ),
            Meal(
                name = "Sushi Roll",
                id = 4L,
                minutes = 45L,
                contributorId = 202L,
                submitted = Date(),
                tags = listOf("Japanese", "Seafood", "Cold"),
                nutrition = Nutrition(250f, 6f, 3f, 400f, 12f, 1f, 35f),
                numberOfSteps = 5,
                steps = listOf(
                    "Cook sushi rice and season it",
                    "Lay nori and rice on bamboo mat",
                    "Add fillings like fish and vegetables",
                    "Roll tightly and slice",
                    "Serve with soy sauce and wasabi"
                ),
                description = "A Japanese delicacy made of vinegared rice and seafood or vegetables.",
                ingredients = listOf("Sushi rice", "Nori", "Raw fish", "Cucumber", "Soy sauce"),
                numberOfIngredients = 5
            ),
            Meal(
                name = "Chicken Biryani",
                id = 6L,
                minutes = 90L,
                contributorId = 203L,
                submitted = Date(),
                tags = listOf("Spiced", "Rice", "Indian"),
                nutrition = Nutrition(520f, 22f, 5f, 360f, 25f, 6f, 50f),
                numberOfSteps = 6,
                steps = listOf(
                    "Marinate chicken with spices",
                    "Cook rice with saffron and spices",
                    "Layer chicken and rice",
                    "Cook over low heat (dum)",
                    "Garnish with coriander",
                    "Serve hot with yogurt"
                ),
                description = "Aromatic rice dish with spiced chicken, saffron, and herbs.",
                ingredients = listOf("Chicken", "Basmati Rice", "Yogurt", "Spices", "Onions", "Garlic"),
                numberOfIngredients = 6
            )
        )
        //when && then
        assertThrows<NoMealsFoundException> {
            getIraqiMealsUseCase()
        }
    }

}