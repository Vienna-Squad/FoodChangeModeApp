package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetIraqiMealsUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
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
    fun `when call getAllMeals() from mealsRepository and it returns list of meals should that getIraqiMealsUseCase return just iraqi meals`() {
        //given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
            createMeal(),
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
            createMeal(),
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
            createMeal(),
            createMeal(),
        )
        //when && then
        assertThat(getIraqiMealsUseCase().size).isEqualTo(4)
    }

    @Test
    fun `when call getAllMeals() from mealsRepository and it returns empty list should getIraqiMealsUseCase throw NoMealsFoundException`() {
        //given
        every { mealsRepository.getAllMeals() } returns emptyList()
        //when && then
        assertThrows<NoMealFoundException> {
            getIraqiMealsUseCase()
        }
    }

    @Test
    fun `when call getAllMeals() from mealsRepository and it returns a list of non-iraqi meals should getIraqiMealsUseCase throw NoMealsFoundException`() {
        //given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(),
            createMeal(),
            createMeal(),
        )
        //when && then
        assertThrows<NoMealFoundException> {
            getIraqiMealsUseCase()
        }
    }

    @Test
    fun `when call getAllMeals() from mealsRepository and it returns a list includes meal without iraqi tag but has iraq in description should still be considered iraqi`() {
        //given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(hasIraqInDescription = true),
            createMeal(),
            createMeal(hasIraqInDescription = true),
            createMeal(),
            createMeal(),
        )
        //when && then
        assertThat(getIraqiMealsUseCase().size).isEqualTo(2)
    }

    @Test
    fun `when call getAllMeals() from mealsRepository and it returns a list includes meal with iraqi tag but has no iraq in description should still be considered iraqi`() {
        //given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(),
            createMeal(hasIraqiTag = true),
            createMeal(),
            createMeal(),
        )
        //when && then
        assertThat(getIraqiMealsUseCase().size).isEqualTo(1)
    }

    private fun createMeal(hasIraqiTag: Boolean = false, hasIraqInDescription: Boolean = false) =
        Meal(
            name = null,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = if (hasIraqiTag) listOf("iraqi") else listOf("egyptian"),
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = if (hasIraqInDescription) "Popular meal in Iraq" else "Popular meal in Egypt",
            ingredients = null,
            numberOfIngredients = null
        )
}