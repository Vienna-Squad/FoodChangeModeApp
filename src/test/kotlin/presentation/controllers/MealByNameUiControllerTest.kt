package presentation.controllers

import com.google.common.truth.Truth.assertThat
import createMeals
import io.mockk.*
import org.example.logic.repository.MealsRepository
import org.example.logic.usecase.GetMealByNameUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.FoodViewer
import org.example.presentation.Interactor
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
    lateinit var MealByNameUiController: MealByNameUiController
    lateinit var interactor: Interactor


    @BeforeEach
    fun setUp(){

        getMealsByNameUseCase= mockk()
        viewer= mockk(relaxed = true)
        interactor= mockk(relaxed = true)
        MealByNameUiController=MealByNameUiController(getMealsByNameUseCase,viewer,interactor)

    }

    @Test
    fun `should show meal details when meal is found`(){
        //given
        every { interactor.getInput() }returns "chinese  candy"

        every { getMealsByNameUseCase.invoke("chinese  candy") }returns createMeals("chinese  candy")

        //when
        MealByNameUiController.execute()

        // then
        verify {
            viewer.showMealDetails(any())
        }

    }


    @Test
    fun `should throw exception when search by name for not available meal `(){

        //given  (stubs)
        every { interactor.getInput() }returns "chinese  candy"

        every { getMealsByNameUseCase.invoke("chinese  candy") } throws NoMealFoundException()

        //when
        MealByNameUiController.execute()

        // then
        verify {
            viewer.showExceptionMessage(any())
        }

    }




}