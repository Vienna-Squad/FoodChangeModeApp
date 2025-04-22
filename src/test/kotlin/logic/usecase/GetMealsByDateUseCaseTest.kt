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
import java.util.*

internal class GetMealsByDateUseCaseTest{


    lateinit var getMealsByDateUseCase: GetMealsByDateUseCase
    lateinit var mealsRepository: MealsRepository

    @BeforeEach
    fun setUp(){

        mealsRepository= mockk()

        getMealsByDateUseCase= GetMealsByDateUseCase(mealsRepository)

    }

    @Test
    fun `should return meals that match the given date `(){

        //given  (stubs)
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeals("chinese  candy", 23933),
            createMeals("fried  potatoes", 37073),
        )
        //when
        val result= getMealsByDateUseCase.invoke(Date(20/7/2003))

        //then
        assertThat(result).containsExactly(
            createMeals("chinese  candy" , 23933),
            createMeals("fried  potatoes" , 37073)
        )


    }

    @Test
    fun `should throw exception when search by date for not available meal `(){

        //given  (stubs)

        every { mealsRepository.getAllMeals() }returns listOf(
            createMeals("alouette  potatoes", 59389),
            createMeals("chinese  candy", 23933),
            createMeals("fried  potatoes", 37073),
            createMeals("apple a day  milk shake", 5289)
        )

        //when && then
        assertThrows<NoMealFoundException> {
            getMealsByDateUseCase.invoke(Date(20/7/2003))
        }

    }
}