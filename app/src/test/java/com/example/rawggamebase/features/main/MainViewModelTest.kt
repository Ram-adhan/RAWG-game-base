package com.example.rawggamebase.features.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.rawggamebase.data.GameRepository
import com.example.rawggamebase.data.dto.Game
import com.example.rawggamebase.data.dto.Result
import com.example.rawggamebase.features.adapters.GameListAdapter
import com.example.rawggamebase.features.list.ListViewModel
import com.example.rawggamebase.features.model.GameModel
import com.example.rawggamebase.utils.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val gameRepo: GameRepository = mockk(relaxed = true)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var uiState: MutableList<UiState<List<GameModel>>>

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = MainViewModel(gameRepo, dispatcher)
        uiState = mutableListOf()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        uiState.clear()
    }

    @Test
    fun `init and returning data`() = runTest {
        val returnedList = listOf(
            Game(
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
            ),
            GameModel(
                viewType = GameListAdapter.VIEW_MORE
            )
        )
        coEvery {
            gameRepo.getGames(searchKey = any(), page = any())
        } coAnswers {
            delay(100)
            Result.Success(data = returnedList)
        }

        val job = launch(dispatcher) {
            viewModel
                .gameList
                .take(3)
                .toList(uiState)
        }

        advanceUntilIdle()
        job.cancel()

        coVerify {
            gameRepo.getGames(null, null)
        }
        assertEquals(UiState.Init, uiState.first())
        assertEquals(UiState.Loading, uiState[1])
        assertEquals(UiState.Success(uiData), uiState.last())
    }

    @Test
    fun `init and returning error`() = runTest {
        val message = "error"
        coEvery {
            gameRepo.getGames(searchKey = any(), page = any())
        } coAnswers {
            delay(100)
            Result.Error(Throwable(message))
        }

        val job = launch(dispatcher) {
            viewModel
                .gameList
                .take(3)
                .toList(uiState)
        }

        advanceUntilIdle()
        job.cancel()

        coVerify {
            gameRepo.getGames(null, null)
        }
        assertEquals(UiState.Init, uiState.first())
        assertEquals(UiState.Loading, uiState[1])
        assertEquals(UiState.Error(message), uiState.last())
    }
}