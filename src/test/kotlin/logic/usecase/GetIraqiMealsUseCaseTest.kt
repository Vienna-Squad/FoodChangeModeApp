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
    private val mealsRepository: MealsRepository = mockk(relaxed = true)
    private val nonIraqiMeals = listOf(
        createMeal(),
        createMeal(),
        createMeal(),
        createMeal(),
    )

    @BeforeEach
    fun setup() {
        getIraqiMealsUseCase = GetIraqiMealsUseCase(mealsRepository)
    }

    @Test
    fun `when call getAllMeals() from mealsRepository and it returns list of meals should that getIraqiMealsUseCase return just iraqi meals`() {
        //given
        val iraqiMeals = listOf(
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
            createMeal(hasIraqiTag = true, hasIraqInDescription = true),
        )
        val mixedMeals = (nonIraqiMeals + iraqiMeals).shuffled()
        every { mealsRepository.getAllMeals() } returns mixedMeals
        //when && then
        assertThat(getIraqiMealsUseCase()).isEqualTo(iraqiMeals)
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
        every { mealsRepository.getAllMeals() } returns nonIraqiMeals
        //when && then
        assertThrows<NoMealFoundException> {
            getIraqiMealsUseCase()
        }
    }

    @Test
    fun `when call getAllMeals() from mealsRepository and it returns a list includes meal without iraqi tag but has iraq in description should still be considered iraqi`() {
        //given iraqiDescribedMeals
        val iraqiDescribedMeals = listOf(
            createMeal(hasIraqInDescription = true),
            createMeal(hasIraqInDescription = true),
        )
        val mixedMeals = (nonIraqiMeals + iraqiDescribedMeals).shuffled()
        every { mealsRepository.getAllMeals() } returns mixedMeals
        //when && then
        assertThat(getIraqiMealsUseCase()).isEqualTo(iraqiDescribedMeals)
    }

    @Test
    fun `when call getAllMeals() from mealsRepository and it returns a list includes meal with iraqi tag but has no iraq in description should still be considered iraqi`() {
        //given
        val iraqiTaggedMeals = listOf(
            createMeal(hasIraqiTag = true),
            createMeal(hasIraqiTag = true),
            createMeal(hasIraqiTag = true),
        )
        val mixedMeals = (nonIraqiMeals + iraqiTaggedMeals).shuffled()
        every { mealsRepository.getAllMeals() } returns mixedMeals
        //when && then
        assertThat(getIraqiMealsUseCase()).isEqualTo(iraqiTaggedMeals)
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