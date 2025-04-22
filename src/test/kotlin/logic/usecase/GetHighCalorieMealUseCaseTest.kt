package logic.usecase


import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.model.Nutrition
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetHighCalorieMealUseCase
import org.example.logic.usecase.exceptions.NullHighCalorieRandomMealException
import org.example.logic.usecase.exceptions.NullRandomMealException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Date

class GetHighCalorieMealUseCaseTest {

    private lateinit var getHighCalorieMealUseCase: GetHighCalorieMealUseCase
    private lateinit var mealsRepository: MealsRepository

    @BeforeEach
    fun setUp() {
        mealsRepository = mockk(relaxed = true)
        getHighCalorieMealUseCase = mockk(relaxed = true)
    }

    @Test
    fun `getNameAndDescription should throw NullRandomMealException if name of meal is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal(null,"desc")
        )
        // when & then
        assertThrows<NullRandomMealException>{getHighCalorieMealUseCase.getNameAndDescription()}
    }
    @Test
    fun `getNameAndDescription should throw NullRandomMealException if description of meal is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal("chicken",null),
        )
        // when & then
        assertThrows<NullRandomMealException>{getHighCalorieMealUseCase.getNameAndDescription()}
    }
    @Test
    fun `getNameAndDescription should throw NullRandomMealException if calories of meal is null or empty`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal("H","desc",null),
        )

        // when & then
        assertThrows<NullRandomMealException>{getHighCalorieMealUseCase.getNameAndDescription()}
    }

    @Test
    fun `getNameAndDescription should return correct high calorie meal if the meal list contain it`() {
        // stubs
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal("chicken","the good meal contain just chicken",600f),
            createMeal("chicken","the good meal contain just chicken",700f),
            createMeal("chicken with more calorie","the good meal contain just chicken",800f)
        )
        // when
        val result = getHighCalorieMealUseCase.getNameAndDescription()
        // then
        assertThat(result).isNotNull()
    }

    @Test
    fun `getMealDetailsByName should throw NullRandomMealException if the name of meal wrong`() {
        // given
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeal("Tomato","hot chicken",600f),
            createMeal("Hot Dog","hot chicken",700f)
        )
        val inputName = "chicken"
        // when
        val result = getHighCalorieMealUseCase.getMealDetailsByName(inputName)
        // then
        assertThrows <NullHighCalorieRandomMealException>{
            result
        }
    }

}


fun createMeal(name: String?,description: String?,calorie: Float? = 800f): Meal{
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

