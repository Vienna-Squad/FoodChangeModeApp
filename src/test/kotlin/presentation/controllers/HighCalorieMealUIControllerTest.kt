package presentation.controllers

import io.mockk.mockk
import io.mockk.verify
import org.example.logic.usecase.GetHighCalorieMealUseCase
import org.example.presentation.Viewer
import org.example.presentation.controllers.HighCalorieMealUIController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HighCalorieMealUIControllerTest {
    private val getHighCalorieMealUseCase: GetHighCalorieMealUseCase = mockk(relaxed = true)
    private val viewer: Viewer = mockk()
    lateinit var highCalorieMealUIController: HighCalorieMealUIController
    @BeforeEach
    fun setUp() {
        highCalorieMealUIController = HighCalorieMealUIController(getHighCalorieMealUseCase,viewer)
    }


    @Test
    fun `execute should call getRandomHighCalorieMeal from getHighCalorieMealUseCase`(){
//        highCalorieMealUIController.execute()
//
//        verify (exactly = 1){ getHighCalorieMealUseCase.getRandomHighCalorieMeal() }
    }

    @Test
    fun `execute should throw InvalidInputNumberOfHighCalorieMeal when the input number is not in range from 1 to 3`() {

        // given
        val inputNumber = 4

        // when&then
//        assertThrows<InvalidInputNumberOfHighCalorieMeal> {
//            highCalorieMealUIController.execute()
//        }

    }

}