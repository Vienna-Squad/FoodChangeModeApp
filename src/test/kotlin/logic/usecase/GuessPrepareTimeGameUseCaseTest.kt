package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.model.Meal
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GuessPrepareTimeGameUseCase
import org.example.logic.usecase.exceptions.*
import org.example.logic.usecase.GuessPrepareTimeGameUseCase.Companion.CONGRATULATION_MESSAGE
import org.example.logic.usecase.exceptions.GameOverException.Companion.GAME_OVER_MESSAGE
import org.example.logic.usecase.exceptions.InvalidAttemptsException.Companion.INVALID_ATTEMPTS_MESSAGE
import org.example.logic.usecase.exceptions.InvalidMinutesException.Companion.INVALID_MINUTES_MESSAGE
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class GuessPrepareTimeGameUseCaseTest {
    private lateinit var guessPrepareTimeGameUseCase: GuessPrepareTimeGameUseCase
    private val mealsRepository: MealsRepository = mockk(relaxed = true)
    private val attempts = 3

    @BeforeEach
    fun setup() {
        guessPrepareTimeGameUseCase = GuessPrepareTimeGameUseCase(
            mealsRepository = mealsRepository
        )
    }

    @Test
    fun `when mealsRepository returns list of meals should getRandomMeal() return random meal from them`() {
        //given
        val meals = listOf(
            createMeal("koshary"),
            createMeal("ta3100"),
            createMeal("molokhya"),
            createMeal("fool"),
        )
        every { mealsRepository.getAllMeals() } returns meals
        //when
        val randomMeal = guessPrepareTimeGameUseCase.getRandomMeal()
        //then
        assertThat(randomMeal).isIn(meals)
    }

    @Test
    fun `when mealsRepository returns empty list should getRandomMeal() throws NoMealFoundException`() {
        //given
        every { mealsRepository.getAllMeals() } returns emptyList()
        //when && then
        assertThrows<NoMealFoundException> {
            guessPrepareTimeGameUseCase.getRandomMeal()
        }
    }

    @Test
    fun `when user guessed correct answer -with allowed attempts- should return congratulation message`() {
        //given
        val randomMeal = createMeal()
        //when
        val message = guessPrepareTimeGameUseCase.guess(30, randomMeal.minutes!!, attempts)
        //then
        assertThat(message).isEqualTo(CONGRATULATION_MESSAGE)
    }

    @Test
    fun `when user guessed incorrect answer -with allowed attempts- greater than the correct answer should throw TooHighException with remaining attempts`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<TooHighException> {
            guessPrepareTimeGameUseCase.guess(60, randomMeal.minutes!!, attempts)
        }
        assertThat(exception.attempt).isEqualTo(attempts - 1)
        assertThat(exception.message).isEqualTo(TooHighException.TOO_HIGH_MESSAGE)
    }

    @Test
    fun `when user guessed incorrect answer -with allowed attempts- less than the correct answer should throw TooHighException with remaining attempts`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<TooLowException> {
            guessPrepareTimeGameUseCase.guess(10, randomMeal.minutes!!, attempts)
        }
        assertThat(exception.attempt).isEqualTo(attempts - 1)
        assertThat(exception.message).isEqualTo(TooLowException.TOO_LOW_MESSAGE)
    }

    @Test
    fun `when user guessed correct answer -with the last attempt- should return congratulation message`() {
        //given
        val randomMeal = createMeal()
        //when
        val message = guessPrepareTimeGameUseCase.guess(30, randomMeal.minutes!!, 1)
        //then
        assertThat(message).isEqualTo(CONGRATULATION_MESSAGE)
    }

    @Test
    fun `when user guessed incorrect answer -with the last attempt- should throw GameOverException with its message`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<GameOverException> {
            guessPrepareTimeGameUseCase.guess(10, randomMeal.minutes!!, 1)
        }
        assertThat(exception.attempt).isEqualTo(0)
        assertThat(exception.message).isEqualTo("$GAME_OVER_MESSAGE ${randomMeal.minutes}")
    }

    @Test
    fun `when user guessed any answer -with negative attempt- should throw InvalidAttemptsException with its message`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<InvalidAttemptsException> {
            guessPrepareTimeGameUseCase.guess(30, randomMeal.minutes!!, -1)
        }
        assertThat(exception.attempt).isEqualTo(0)
        assertThat(exception.message).isEqualTo(INVALID_ATTEMPTS_MESSAGE)
    }

    @Test
    fun `when user guessed any answer -with ZERO attempts- should throw InvalidAttemptsException with its message`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<InvalidAttemptsException> {
            guessPrepareTimeGameUseCase.guess(30, randomMeal.minutes!!, 0)
        }
        assertThat(exception.attempt).isEqualTo(0)
        assertThat(exception.message).isEqualTo(INVALID_ATTEMPTS_MESSAGE)
    }

    @Test
    fun `when user guessed with NEGATIVE minutes as a answer -with allowed attempts- should throw InvalidMinutesException with its message`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<InvalidMinutesException> {
            guessPrepareTimeGameUseCase.guess(-30, randomMeal.minutes!!, attempts)
        }
        assertThat(exception.message).isEqualTo(INVALID_MINUTES_MESSAGE)
    }

    @Test
    fun `when user guessed with NEGATIVE minutes as a answer -with not allowed attempts- should throw InvalidAttemptsException with its message`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<InvalidAttemptsException> {
            guessPrepareTimeGameUseCase.guess(-30, randomMeal.minutes!!, 0)
        }
        assertThat(exception.message).isEqualTo(INVALID_ATTEMPTS_MESSAGE)
    }

    @Test
    fun `when user guessed incorrect answer greater than the correct one -with the last attempt- should throw GameOverException with its message`() {
        //given
        val randomMeal = createMeal()
        val givenException = GameOverException(correctAnswer = randomMeal.minutes!!)
        //when && then
        val exception = assertThrows<GameOverException> {
            guessPrepareTimeGameUseCase.guess(60, randomMeal.minutes, 1)
        }
        assertThat(exception.message).isEqualTo(givenException.message)
    }

    private fun createMeal(name: String? = null) =
        Meal(
            name = name,
            id = null,
            minutes = 30,
            contributorId = null,
            submitted = Date(),
            tags = listOf("egyptian", "spicy"),
            nutrition = null,
            numberOfSteps = null,
            steps = null,
            description = "Popular meal in Egypt",
            ingredients = null,
            numberOfIngredients = null
        )
}