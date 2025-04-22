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

    val localDate = LocalDate.of(2003, 4, 14)
    val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())



    @BeforeEach
    fun setUp(){

        mealsRepository= mockk()
        kmpSearcher= mockk()

        getMealsByNameUseCase= GetMealByNameUseCase(mealsRepository,kmpSearcher)


    }

    @Test
    fun `should return meals that match the given name`(){

        //given  (stubs)


        every { mealsRepository.getAllMeals() } returns listOf(
            createMeals("chinese  candy", date),
            createMeals("fried  potatoes", date),

        )
        every { kmpSearcher.search("chinese  candy", "chinese  candy") } returns true
        every { kmpSearcher.search("fried  potatoes", "chinese  candy") } returns false
        every { kmpSearcher.search("chinese  candy", "ChInese  CAndy") } returns true


        val mealName="chinese  candy"

        //when
        val result= getMealsByNameUseCase.invoke(mealName)

        //then
        assertThat(result).isEqualTo(
            createMeals(mealName, date),
        )

    }

    @Test
    fun `should throw exception when search by name for not available meal `(){

        //given  (stubs)
        every { mealsRepository.getAllMeals() } returns listOf(
            createMeals("chinese  candy", date),
            createMeals("fried  potatoes", date),
            createMeals("apple a day  milk shake", date)
        )
        every { kmpSearcher.search("chinese  candy", "alouette  potatoes") } returns false
        every { kmpSearcher.search("fried  potatoes", "alouette  potatoes") } returns false
        every { kmpSearcher.search("apple a day  milk shake", "alouette  potatoes") } returns false


        //when && then
        assertThrows<NoMealFoundException> {
            getMealsByNameUseCase.invoke("alouette  potatoes")
        }

    }

}