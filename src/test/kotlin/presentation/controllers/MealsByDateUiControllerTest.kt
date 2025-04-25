package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecase.createMeals
import org.example.logic.model.Meal
import org.example.logic.usecase.GetMealsByDateUseCase
import org.example.logic.usecase.exceptions.NoMealFoundException
import org.example.presentation.controllers.MealsByDateUiController
import org.example.utils.interactor.Interactor
import org.example.utils.viewer.ExceptionViewer
import org.example.utils.viewer.ItemDetailsViewer
import org.example.utils.viewer.ItemsViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

internal class MealsByDateUiControllerTest{

    lateinit var getMealsByDateUseCase: GetMealsByDateUseCase
    lateinit var MealsByDateUiController: MealsByDateUiController
    lateinit var interactor: Interactor
    lateinit var viewer: ItemsViewer<Meal>
    lateinit var exceptionViewer: ExceptionViewer
    lateinit var itemViewer:ItemDetailsViewer<Meal>




    private fun localDateToDate(year: Int, month: Int, day: Int): Date =
        LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant().let { Date.from(it) }

    private val date1 = localDateToDate(2003, 4, 14)
    private val date2 = localDateToDate(2000, 4, 14)
    private val dateNotFound = localDateToDate(2003, 7, 20)

    val meals= listOf(
        createMeals("chinese  candy",37073 , date1),
        createMeals("fried  potatoes", 23933, date2),
        createMeals("apple a day  milk shake", 5289, date1)


    )
    @BeforeEach
    fun setUp(){
        getMealsByDateUseCase= mockk(relaxed = true)
        viewer= mockk(relaxed = true)
        itemViewer= mockk(relaxed = true)
        exceptionViewer= mockk(relaxed = true)
        interactor= mockk()
        MealsByDateUiController= MealsByDateUiController(getMealsByDateUseCase,exceptionViewer,viewer,itemViewer,interactor)
    }

    @Test
    fun `should return meals when the given date matches the meal's date`() {


        //given  (stubs)
        every { interactor.getInput() }returns "14/4/2003"
        every { getMealsByDateUseCase(date1) } returns listOf(meals[0],meals[2])


        //when
        MealsByDateUiController.execute()


        //then
        verify {
            viewer.viewItems(listOf(meals[0],meals[2]))
        }


    }

    @Test
    fun `should throw no meal found exception when no meals are found for the given date `(){

        //given  (stubs)

        every { interactor.getInput() }returns "20/7/2003"

        every { getMealsByDateUseCase(dateNotFound) }throws NoMealFoundException()


        // when
        MealsByDateUiController.execute()

        // then
        verify {
            exceptionViewer.viewExceptionMessage(
                match { it is NoMealFoundException }
            )
        }

    }
    @Test
    fun `should throw incorrect date format exception when date format is invalid `(){

        //given  (stubs)
        every { interactor.getInput() }returns "candy"

        // when
        MealsByDateUiController.execute()

        // then
        verify {
            exceptionViewer.viewExceptionMessage(any())
        }

    }

    @Test
    fun `should return meal description when the given id matches a meal on the selected date`(){


        //given  (stubs)
        every { interactor.getInput() } returns "14/4/2003" andThen "37073"
        every { getMealsByDateUseCase(date1) } returns listOf(meals[0],meals[2])


        //when
        MealsByDateUiController.execute()

        //then
        verify {
            itemViewer.viewDetails(meals[0])
        }



    }
    @Test
    fun `should return no meal found exception when the given id not matches a meal on the selected date`(){

        // given
        every { interactor.getInput() } returns "14/4/2003" andThen "37074" // ID not found
        every { getMealsByDateUseCase(date1) } returns listOf(meals[0],meals[2])

        //when
        MealsByDateUiController.execute()

        //then
        verify {
            exceptionViewer.viewExceptionMessage(
                match { it is NoMealFoundException }
            )
        }



    }




}