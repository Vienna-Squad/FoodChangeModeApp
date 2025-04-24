package presentation.controllers

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecase.createMeals
import org.checkerframework.checker.units.qual.m
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.FoodViewer
import org.example.presentation.Interactor
import org.example.presentation.Viewer
import org.example.presentation.controllers.MealsByDateUiController
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

internal class MealsByDateUiControllerTest{

    lateinit var getMealsByDateUseCase: GetMealsByDateUseCase
    lateinit var MealsByDateUiController: MealsByDateUiController
    lateinit var interactor: Interactor
    lateinit var viewer: FoodViewer



    private fun localDateToDate(year: Int, month: Int, day: Int): Date =
        LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant().let { Date.from(it) }

    private val date1 = localDateToDate(2003, 4, 14)
    private val date2 = localDateToDate(2000, 4, 14)
    private val dateNotFound = localDateToDate(2003, 7, 20)

    @BeforeEach
    fun setUp(){
        getMealsByDateUseCase= mockk(relaxed = true)
        viewer= mockk(relaxed = true)
        interactor= mockk()
        MealsByDateUiController= MealsByDateUiController(getMealsByDateUseCase,viewer,interactor)
    }

    @Test
    fun `should return meals when the given date matches the submitted date in meal`() {
        val meals= listOf(
        createMeals("chinese  candy", 23933, date1),
        createMeals("fried  potatoes", 37073, date1),
        )

        //given  (stubs)
        every { interactor.getInput() }returns "14/4/2003"
        every { getMealsByDateUseCase(date1) } returns meals


        //when
        MealsByDateUiController.execute()

        //then
        assertThat(getMealsByDateUseCase(date1)).isEqualTo(meals)

    }

    @Test
    fun `should throw exception when search by date for not available meal `(){

        //given  (stubs)

        val exception=NoMealFoundException()
        every { interactor.getInput() }returns "20/7/2003"

        every { getMealsByDateUseCase(dateNotFound) }throws exception


        // when
        MealsByDateUiController.execute()

        // then
       verify {
           viewer.showExceptionMessage(exception)
       }

    }
    @Test
    fun `should throw exception when invalid date format `(){

        //given  (stubs)
        every { interactor.getInput() }returns "candy"

        // when
        MealsByDateUiController.execute()

        // then
        verify {
            viewer.showExceptionMessage(any())
        }

    }

    @Test
    fun `should return meals description when the given id matches a meal in the list`(){

        val meals= listOf(
            createMeals("fried  potatoes", 37073, date1),
        )

        //given  (stubs)
        every { interactor.getInput() }returns "37073"
        every { getMealsByDateUseCase(date1) } returns meals


        //when
        MealsByDateUiController.execute()

        //then
        assertThat(getMealsByDateUseCase(date1)).isEqualTo(meals)


    }
    @Test
    fun `should return exception when the given id not matches with the meal in the list`(){

        val exception=NoMealFoundException()

        //given  (stubs)
        every { interactor.getInput() } returns "14/4/2003" andThen "37074"
        every { getMealsByDateUseCase(any()) } throws exception


        //when
        MealsByDateUiController.execute()

        //then
        verify {
            viewer.showExceptionMessage(exception)
        }


    }




}