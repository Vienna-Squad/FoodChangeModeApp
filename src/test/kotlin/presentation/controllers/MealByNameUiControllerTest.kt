package presentation.controllers

import createMeals
import io.mockk.*
import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealByNameUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.MealByNameUiController
import org.example.utils.interactor.Interactor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MealByNameUiControllerTest{


    lateinit var getMealsByNameUseCase: GetMealByNameUseCase
    lateinit var viewer:ItemDetailsViewer<Meal>
    lateinit var MealByNameUiController: MealByNameUiController
    lateinit var interactor: Interactor
    lateinit var exceptionViewer: ExceptionViewer

    val meal= listOf(
        createMeals("chinese  candy"),
        createMeals("fried potato")
    )


    @BeforeEach
    fun setUp(){

        getMealsByNameUseCase= mockk(relaxed = true)
        viewer= mockk(relaxed = true)
        exceptionViewer= mockk(relaxed = true)
        interactor= mockk()
        MealByNameUiController=MealByNameUiController(getMealsByNameUseCase,viewer,exceptionViewer,interactor)

    }

    @Test
    fun `should display meal details when a meal is found by name`(){

        //given
        every { interactor.getInput() }returns "chinese  candy"

        every { getMealsByNameUseCase("chinese  candy") }returns meal[0]

        //when
        MealByNameUiController.execute()

        // then
        verify {
            viewer.viewDetails(meal[0])
        }

    }


    @Test
    fun `should show no meal found exception message when no meal is found for the given name`(){

        //given  (stubs)
        every { interactor.getInput() }returns "not-exist"

        every { getMealsByNameUseCase("not-exist") } throws NoMealFoundException()

        //when
        MealByNameUiController.execute()

        // then
        verify {
            exceptionViewer.viewExceptionMessage(
                match {
                it is NoMealFoundException
            })
        }

    }




}