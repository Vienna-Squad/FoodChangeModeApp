package logic.usecase

import com.google.common.truth.Truth.assertThat
import createMeals
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetHealthyFastFoodUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


internal class GetHealthyFastFoodUseCaseTest {

    lateinit var getHealthyFastFoodUseCase: GetHealthyFastFoodUseCase
    lateinit var mealsRepository: MealsRepository

    val localDate = LocalDate.of(2003, 4, 14)
    val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())


    @BeforeEach
    fun setUp() {

        mealsRepository = mockk()
        getHealthyFastFoodUseCase = GetHealthyFastFoodUseCase(mealsRepository)
    }

    @Test
    fun `should return only healthy and quick meals`() {
        //given (stubs)

        every { mealsRepository.getAllMeals() } returns listOf(
            Meal(
                "chinese candy", submitted = date,
                id = null,
                tags =null,
                minutes = 10L,
                contributorId = 1L,
                numberOfSteps = 3,
                steps = listOf("Step 1", "Step 2", "Step 3"),
                description = "A delicious and healthy meal.",
                ingredients = listOf("Ingredient 1", "Ingredient 2"),
                numberOfIngredients = 2,
                nutrition = Nutrition(
                    totalFat = 15f,
                    saturatedFat = 10f,
                    carbohydrates = 20f,
                    calories = 200f,
                    sugar = 5f,
                    sodium = 150f,
                    protein = 10f
                ),
                ),
            Meal(
                "Bad Food", submitted = date,
                id = null,
                tags = null,
                minutes =30L,
                contributorId = 1L,
                numberOfSteps = 3,
                steps = listOf("Step 1", "Step 2", "Step 3"),
                description = "A delicious and healthy meal.",
                ingredients = listOf("Ingredient 1", "Ingredient 2"),
                numberOfIngredients = 2,
                nutrition = Nutrition(
                    totalFat = 30f,
                    saturatedFat = 40f,
                    carbohydrates = 50f,
                    calories = 200f,
                    sugar = 5f,
                    sodium = 150f,
                    protein = 10f
                ),
            ),
            )
        //when
        val result: List<Meal> = getHealthyFastFoodUseCase()

        //then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("chinese candy")

    }
}
