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
    fun `should return random meal from non-empty list`() {
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
    fun `should throw NoMealFoundException when no meals found`() {
        //given
        every { mealsRepository.getAllMeals() } returns emptyList()
        //when && then
        assertThrows<NoMealFoundException> {
            guessPrepareTimeGameUseCase.getRandomMeal()
        }
    }

    @Test
    fun `should return success when guess is correct`() {
        //given
        val randomMeal = createMeal()
        //when
        val message = guessPrepareTimeGameUseCase.guess(30, randomMeal.minutes!!, attempts)
        //then
        assertThat(message).isEqualTo(CONGRATULATION_MESSAGE)
    }

    @Test
    fun `should throw TooHighException when guess is too high`() {
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
    fun `should throw TooLowException when guess is too low`() {
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
    fun `should return success when correct on last attempt`() {
        //given
        val randomMeal = createMeal()
        //when
        val message = guessPrepareTimeGameUseCase.guess(30, randomMeal.minutes!!, 1)
        //then
        assertThat(message).isEqualTo(CONGRATULATION_MESSAGE)
    }

    @Test
    fun `should throw GameOverException when wrong on last attempt`() {
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
    fun `should throw InvalidAttemptsException when attempts is negative`() {
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
    fun `should throw InvalidAttemptsException when attempts is zero`() {
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
    fun `should throw InvalidMinutesException when guess is negative`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<InvalidMinutesException> {
            guessPrepareTimeGameUseCase.guess(-30, randomMeal.minutes!!, attempts)
        }
        assertThat(exception.message).isEqualTo(INVALID_MINUTES_MESSAGE)
    }

    @Test
    fun `should prioritize InvalidAttemptsException over InvalidMinutesException`() {
        //given
        val randomMeal = createMeal()
        //when && then
        val exception = assertThrows<InvalidAttemptsException> {
            guessPrepareTimeGameUseCase.guess(-30, randomMeal.minutes!!, 0)
        }
        assertThat(exception.message).isEqualTo(INVALID_ATTEMPTS_MESSAGE)
    }

    @Test
    fun `should throw GameOverException when too high on last attempt`() {
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