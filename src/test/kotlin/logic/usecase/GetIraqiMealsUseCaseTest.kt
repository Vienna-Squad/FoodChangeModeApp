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
    fun `should return only iraqi meals from mixed list`() {
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
    fun `should throw NoMealsFoundException when meal list is empty`() {
        //given
        every { mealsRepository.getAllMeals() } returns emptyList()
        //when && then
        assertThrows<NoMealFoundException> {
            getIraqiMealsUseCase()
        }
    }

    @Test
    fun `should throw NoMealsFoundException when no iraqi meals found`() {
        //given
        every { mealsRepository.getAllMeals() } returns nonIraqiMeals
        //when && then
        assertThrows<NoMealFoundException> {
            getIraqiMealsUseCase()
        }
    }

    @Test
    fun `should detect iraqi meals by description only`() {
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
    fun `should detect iraqi meals by tag only`() {
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


    @Test
    fun `should skip meals with null tags`() {
        //given
        val iraqiMeals = listOf(
            createMeal(hasIraqiTag = true),
            createMeal(hasIraqiTag = null),
            createMeal(hasIraqInDescription = true),
            createMeal(hasIraqiTag = true, hasIraqInDescription = null),
        )
        every { mealsRepository.getAllMeals() } returns iraqiMeals
        //when && then
        assertThat(getIraqiMealsUseCase().size).isEqualTo(3)
    }

    @Test
    fun `should skip meals with null descriptions`() {
        //given
        val iraqiMeals = listOf(
            createMeal(hasIraqiTag = true),
            createMeal(hasIraqInDescription = null),
            createMeal(hasIraqInDescription = true),
            createMeal(hasIraqiTag = null, hasIraqInDescription = true),
        )
        every { mealsRepository.getAllMeals() } returns iraqiMeals
        //when && then
        assertThat(getIraqiMealsUseCase().size).isEqualTo(3)
    }


    private fun createMeal(hasIraqiTag: Boolean? = false, hasIraqInDescription: Boolean? = false) =
        Meal(
            name = null,
            id = null,
            minutes = null,
            contributorId = null,
            submitted = Date(),
            tags = if (hasIraqiTag == null) null else if (hasIraqiTag) listOf("iraqi") else listOf("egyptian"),
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = if (hasIraqInDescription == null) null else if (hasIraqInDescription) "Popular meal in Iraq" else "Popular meal in Egypt",
            ingredients = null,
            numberOfIngredients = null
        )
}