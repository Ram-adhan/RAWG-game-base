package com.example.rawggamebase.features.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.GameRepository
import com.example.data.dto.Game
import com.example.data.dto.Result
import com.example.rawggamebase.features.model.GameModel
import com.example.rawggamebase.utils.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    private lateinit var viewModel: ListViewModel
    private val gameRepo: com.example.data.GameRepository = mockk(relaxed = true)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var uiState: MutableList<UiState<List<GameModel>>>

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = ListViewModel(gameRepo, dispatcher)
        uiState = mutableListOf()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        uiState.clear()
    }

    @Test
    fun `search game when success return list`() = runTest {
        val keyword = "Hello"
        val returnedList = listOf(
            com.example.data.dto.Game(
                id = 1,
                name = "Hello",
                backgroundImage = "backgroundImage",
                rating = 4.5,
                released = "14-14-2020"
            )
        )
        val uiData = listOf(
            GameModel(
                id = 1,
                title = "Hello",
                imageLink = "backgroundImage",
                rating = "4.5",
                releaseDate = "14-14-2020"
            )
        )
        coEvery {
            gameRepo.getGames(searchKey = any(), page = any())
        } returns com.example.data.dto.Result.Success(data = returnedList)

        val job = launch {
            viewModel.gameList.toList(uiState)
        }
        viewModel.searchGames(keyword = keyword)
        advanceUntilIdle()
        job.cancel()
        coVerify {
            gameRepo.getGames(searchKey = keyword, page = 1)
        }
        assertEquals(UiState.Init, uiState[0])
        assertEquals(UiState.Success(uiData), uiState[1])
    }

    @Test
    fun `search game when get Error return Error Message`() = runTest {
        val message = "This is Error"
        val keyword = "Hello"
        coEvery {
            gameRepo.getGames(searchKey = any(), page = any())
        } returns com.example.data.dto.Result.Error(error = Throwable(message))

        val job = launch {
            viewModel.gameList.toList(uiState)
        }
        viewModel.searchGames(keyword = keyword)
        advanceUntilIdle()
        job.cancel()
        coVerify {
            gameRepo.getGames(searchKey = keyword, page = 1)
        }
        assertEquals(UiState.Init, uiState[0])
        assertEquals(UiState.Error(message), uiState[1])
        assertEquals(1, viewModel.currentPage)
    }

    @Test
    fun `onScrollPage when success return list`() = runTest {
        val returnedList = listOf(
            com.example.data.dto.Game(
                id = 1,
                name = "Hello",
                backgroundImage = "backgroundImage",
                rating = 4.5,
                released = "14-14-2020"
            )
        )
        val uiData = listOf(
            GameModel(
                id = 1,
                title = "Hello",
                imageLink = "backgroundImage",
                rating = "4.5",
                releaseDate = "14-14-2020"
            )
        )
        val currentPage = 1
        coEvery {
            gameRepo.getGames(searchKey = any(), page = any())
        } coAnswers {
            delay(100) // simulate delay for calling
            com.example.data.dto.Result.Success(data = returnedList)
        }

        val job = launch {
            viewModel.gameList.toList(uiState)
        }

        viewModel.onScrollPage()
        advanceUntilIdle()
        job.cancel()

        assertEquals(UiState.Loading, uiState[0])
        coVerify {
            gameRepo.getGames(searchKey = null, page = currentPage + 1)
        }
        assertEquals(UiState.Success(uiData), uiState[1])
        assertEquals(currentPage + 1, viewModel.currentPage)
    }

    @Test
    fun `onScrollPage when get Error return Error Message`() = runTest {
        val message = "This is Error"
        val page = 2
        coEvery {
            gameRepo.getGames(searchKey = any(), page = any())
        } coAnswers {
            delay(100)
            com.example.data.dto.Result.Error(error = Throwable(message))
        }

        val job = launch {
            viewModel.gameList.toList(uiState)
        }
        viewModel.onScrollPage()
        advanceUntilIdle()
        coVerify {
            gameRepo.getGames(searchKey = any(), page = page)
        }
        job.cancel()
        assertEquals(UiState.Loading, uiState[0])
        assertEquals(UiState.Error(message), uiState[1])
        assertEquals(1, viewModel.currentPage)
    }
}