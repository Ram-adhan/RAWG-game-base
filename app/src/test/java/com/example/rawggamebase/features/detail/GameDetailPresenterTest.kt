package com.example.rawggamebase.features.detail

import com.example.rawggamebase.data.GameRepository
import com.example.rawggamebase.data.dto.AddedByStatus
import com.example.rawggamebase.data.dto.Developer
import com.example.rawggamebase.data.dto.GameDetail
import com.example.rawggamebase.data.dto.Result
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GameDetailPresenterTest {
    private lateinit var presenter: GameDetailPresenter

    @RelaxedMockK
    lateinit var view: GameDetailView
    private val gameRepo: GameRepository = mockk(relaxed = true)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        presenter = GameDetailPresenter(gameRepo)
        presenter.onAttach(view)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test get game detail`() = runTest {
        val id = 1
        val gameDetail = GameDetail(
            id = 1,
            name = "Game name",
            descriptionRaw = "descriptionRaw",
            rating = 4.5,
            backgroundImage = "background Image",
            released = "13-13-2020",
            developers = listOf(
                Developer(id = 2, name = "Developer game")
            ),
            addedByStatus = AddedByStatus(
                yet = 10,
                owned = 10,
                beaten = 10,
                toplay = 10,
                dropped = 10,
                playing = 10
            )
        )

        coEvery {
            gameRepo.getGameDetail(id)
        } coAnswers {
            delay(10)
            Result.Success(gameDetail)
        }

        presenter.getGameDetail(id)
        advanceUntilIdle()

        coVerify {
            gameRepo.getGameDetail(id)
        }

        coVerify {
            view.onProgress()
            view.onSuccessGetDetail(gameDetail.toGameDetailModel())
            view.onFinishProgress()
        }
    }
}