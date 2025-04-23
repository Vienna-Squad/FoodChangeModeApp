package presentation.controllers

import com.google.common.truth.Truth.assertThat
import createMeals
import io.mockk.*
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetMealByNameUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.FoodViewer
import org.example.presentation.UiController
import org.example.presentation.controllers.MealByNameUiController
import org.example.utils.KMPSearcher
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

internal class MealByNameUiControllerTest{


    lateinit var getMealsByNameUseCase: GetMealByNameUseCase
    lateinit var viewer: FoodViewer
    lateinit var getMealByNameUiController: MealByNameUiController


    val localDate = LocalDate.of(2003, 4, 14)
    val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

    @BeforeEach
    fun setUp(){

        getMealsByNameUseCase= mockk()
        viewer= mockk(relaxed = true)
        getMealByNameUiController=MealByNameUiController(getMealsByNameUseCase,viewer)

    }

    @Test
    fun `should show meal details when meal is found`(){
        //given
        every { getMealsByNameUseCase.invoke("chinese  candy") }returns createMeals("chinese  candy", date)

        //when
        System.setIn("chinese  candy\n".byteInputStream())
        getMealByNameUiController.execute()

        // then
        verify {
            viewer.showMealDetails(createMeals("chinese  candy", date))
        }

    }

    @Test
    fun `should return exception when meal is not found`(){
        //given
        every { getMealsByNameUseCase.invoke("chinese  candy") }returns createMeals("chinese  candy", date)
        every { getMealsByNameUseCase.invoke("fried  potatoes") } throws NoMealFoundException("Meal not found")


        System.setIn("fried  potatoes\n".byteInputStream())

        mockkStatic("kotlin.io.ConsoleKt")
        every { println("Meal not found") } just Runs

        //when
        getMealByNameUiController.execute()

        // then
        verify {
            println("Meal not found")
        }


    }




}