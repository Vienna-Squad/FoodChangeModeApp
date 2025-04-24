package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

internal class GetMealsByDateUseCaseTest{


    lateinit var getMealsByDateUseCase: GetMealsByDateUseCase
    lateinit var mealsRepository: MealsRepository

    private fun localDateToDate(year: Int, month: Int, day: Int): Date =
        LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant().let { Date.from(it) }

    private val date1 = localDateToDate(2003, 4, 14)
    private val date2 = localDateToDate(2000, 4, 14)
    private val dateNotFound = localDateToDate(2003, 7, 20)

    val meals=listOf(
        createMeals("chinese  candy", 23933, date1),
        createMeals("fried  potatoes", 37073, date2),
        createMeals("apple a day  milk shake", 5289, date1)


    )

    @BeforeEach
    fun setUp(){

        mealsRepository= mockk()

        getMealsByDateUseCase= GetMealsByDateUseCase(mealsRepository)

    }

    @Test
    fun `should return meals that match the given date `(){

        //given  (stubs)
        every { mealsRepository.getAllMeals() } returns meals

        //when
        val result= getMealsByDateUseCase(date1)

        //then
        assertThat(result).containsExactly(meals[0],meals[2])
    }

    @Test
    fun `should throw exception when search by date for not available meal `(){

        //given  (stubs)
        every { mealsRepository.getAllMeals() }returns meals

        //when && then
        assertThrows<NoMealFoundException> {
            getMealsByDateUseCase(dateNotFound)
        }

    }
}