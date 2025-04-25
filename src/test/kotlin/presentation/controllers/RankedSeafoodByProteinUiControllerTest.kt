package presentation.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.logic.model.RankedMealResult
import org.example.logic.usecase.GetRankedSeafoodByProteinUseCase
import org.example.logic.usecase.exceptions.NoSeafoodFoundException
import org.example.presentation.Viewer
import org.junit.jupiter.api.Test
import org.example.presentation.controllers.RankedSeafoodByProteinUiController
import java.io.ByteArrayOutputStream
import java.io.PrintStream

import org.junit.jupiter.api.*


class RankedSeafoodByProteinUiControllerTest {

    private lateinit var useCase: GetRankedSeafoodByProteinUseCase
    private lateinit var viewer: Viewer
    private lateinit var controller: RankedSeafoodByProteinUiController

    private val stdOut = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        controller = RankedSeafoodByProteinUiController(useCase, viewer)
        System.setOut(PrintStream(stdOut))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        stdOut.reset()
    }

    @Test
    fun `given valid seafood results when execute is called then display ranked meals with protein`() {
        val results = listOf(
            RankedMealResult(rank = 1, name = "Salmon", protein = 25f),
            RankedMealResult(rank = 2, name = "Tuna", protein = 22f)
        )
        every { useCase.invoke() } returns results

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("Salmon"))
        assert(output.contains("Tuna"))
        assert(output.contains("Protein (g)"))
        verify(exactly = 0) { viewer.showExceptionMessage(any()) }
    }

    @Test
    fun `given empty seafood list when execute is called then display no meals found message`() {
        every { useCase.invoke() } returns emptyList()

        controller.execute()

        val output = stdOut.toString()
        assert(output.contains("No seafood meals with protein information found"))
        verify(exactly = 0) { viewer.showExceptionMessage(any()) }
    }

    @Test
    fun `given NoSeafoodFoundException when execute is called then viewer should display the exception message`() {
        val exception = NoSeafoodFoundException("No seafood found")
        every { useCase.invoke() } throws exception

        controller.execute()

        verify { viewer.showExceptionMessage(exception) }
    }

    @Test
    fun `given generic exception when execute is called then viewer should display the exception message`() {
        val exception = RuntimeException("Something went wrong")
        every { useCase.invoke() } throws exception

        controller.execute()

        verify { viewer.showExceptionMessage(exception) }
    }
}