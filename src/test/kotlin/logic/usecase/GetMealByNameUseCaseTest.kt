package logic.usecase

import com.google.common.truth.Truth.assertThat
import createMeals
import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetMealByNameUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.utils.KMPSearcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.ZoneId
import java.util.*



internal class GetMealByNameUseCaseTest{

    lateinit var getMealsByNameUseCase: GetMealByNameUseCase
    lateinit var mealsRepository: MealsRepository
    lateinit var kmpSearcher: KMPSearcher

    @BeforeEach
    fun setUp(){

        mealsRepository= mockk()
        kmpSearcher= mockk()

        getMealsByNameUseCase= GetMealByNameUseCase(mealsRepository,kmpSearcher)


    }

    val meals=listOf(
        createMeals("chinese  candy"),
        createMeals("fried  potatoes"),

        )

    @Test
    fun `should return meal when the input name matches a meal in the list`(){

        //given  (stubs)
        every { mealsRepository.getAllMeals() } returns meals
        every { kmpSearcher.search("chinese  candy", "chinese  candy") } returns true
        every { kmpSearcher.search("fried  potatoes", "chinese  candy") } returns false


        val mealName="chinese  candy"

        //when
        val result= getMealsByNameUseCase(mealName)

        //then
        assertThat(result).isEqualTo(meals[0])


    }

    @Test
    fun `should return the meal when name matches regardless of case`(){

        //given  (stubs)
        every { mealsRepository.getAllMeals() } returns meals
        every { kmpSearcher.search("chinese  candy", "chinese  candy") } returns true
        every { kmpSearcher.search("chinese  candy", "ChInesE  CAndy") } returns true


        val mealName="ChInesE  CAndy"

        //when
        val result= getMealsByNameUseCase(mealName)

        //then
        assertThat(result).isEqualTo(meals[0])

    }

    @Test
    fun `should throw no meal found exception when no meal matches the input name`(){

        //given  (stubs)
        every { mealsRepository.getAllMeals() } returns meals
        every { kmpSearcher.search("chinese  candy", "alouette  potatoes") } returns false
        every { kmpSearcher.search("fried  potatoes", "alouette  potatoes") } returns false


        //when && then
        assertThrows<NoMealFoundException> {
            getMealsByNameUseCase("alouette  potatoes")
        }

    }

}