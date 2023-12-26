package com.example.rawggamebase.features.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.GameRepository
import com.example.data.dto.AddedByStatus
import com.example.data.dto.Developer
import com.example.data.dto.GameDetail
import com.example.data.dto.Result
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
class GameDetailViewModelTest {
    private lateinit var viewModel: GameDetailViewModel
    private val gameRepo: com.example.data.GameRepository = mockk(relaxed = true)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var uiState: MutableList<UiState<GameDetailModel>>

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = GameDetailViewModel(gameRepo, dispatcher)
        uiState = mutableListOf()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        uiState.clear()
    }

    @Test
    fun `get detail game and return success`() = runTest {
        val id = 1
        val gameDetail = com.example.data.dto.GameDetail(
            id = 1,
            name = "Game name",
            descriptionRaw = "descriptionRaw",
            rating = 4.5,
            backgroundImage = "background Image",
            released = "13-13-2020",
            developers = listOf(
                com.example.data.dto.Developer(id = 2, name = "Developer game")
            ),
            addedByStatus = com.example.data.dto.AddedByStatus(
                yet = 10,
                owned = 10,
                beaten = 10,
                toplay = 10,
                dropped = 10,
                playing = 10
            )
        )
        coEvery {
            gameRepo.getGameDetail(any())
        } coAnswers {
            delay(10)
            com.example.data.dto.Result.Success(gameDetail)
        }

        val job = launch {
            viewModel.getGameDetail(id)
                .take(3)
                .toList(uiState)
        }

        advanceUntilIdle()
        job.cancel()

        coVerify {
            gameRepo.getGameDetail(id)
        }
        assertEquals(UiState.Loading, uiState.first())
        assertEquals(
            UiState.Success(
                GameDetailModel(
                    id = 1,
                    title = "Game name",
                    coverImage = "background Image",
                    developer = "Developer game",
                    rating = "4.5",
                    releaseDate = "13-13-2020",
                    totalPlayed = "40",
                    description = "descriptionRaw"
                )
            ), uiState.last()
        )
    }

    @Test
    fun `get detail game and return error`() = runTest {
        val id = 1
        val message = "Error"
        coEvery {
            gameRepo.getGameDetail(any())
        } coAnswers {
            delay(10)
            com.example.data.dto.Result.Error(Throwable(message))
        }

        val job = launch {
            viewModel.getGameDetail(id)
                .take(3)
                .toList(uiState)
        }

        advanceUntilIdle()
        job.cancel()

        coVerify {
            gameRepo.getGameDetail(id)
        }
        assertEquals(UiState.Loading, uiState.first())
        assertEquals(UiState.Error(message), uiState.last())
    }
}