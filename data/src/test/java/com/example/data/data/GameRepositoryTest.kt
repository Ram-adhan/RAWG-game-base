package com.example.data.data

import com.example.data.GameRepository
import com.example.data.dto.BaseListResponse
import com.example.data.dto.Game
import com.example.data.dto.Result
import com.example.data.services.GamesApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class GameRepositoryTest {

    private lateinit var gameRepository: GameRepository
    private val gameApi: GamesApi = mockk()

    @Before
    fun setUp() {
        gameRepository = GameRepository(gameApi)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getGame return success`() = runTest {
        val listGame = listOf(Game())
        val response = BaseListResponse(
            results = listGame
        )
        coEvery {
            gameApi.getGames(any(), any(), any())
        } returns Response.success(response)

        val result = gameRepository.getGames(null, null)

        coVerify {
            gameApi.getGames(null, null, 20)
        }

        assert(result is Result.Success)
        assertEquals(
            Result.Success(listGame).data,
            (result as Result.Success).data
        )
    }

    @Test
    fun `getGame return success with has next indicator set to true`() = runTest {
        val listGame = listOf(Game())
        val response = BaseListResponse(
            next = "true",
            results = listGame
        )
        coEvery {
            gameApi.getGames(any(), any(), any())
        } returns Response.success(response)

        val result = gameRepository.getGames(null, null)

        coVerify {
            gameApi.getGames(null, null, 20)
        }

        assert(result is Result.Success)
        assertEquals(listGame, (result as Result.Success).data)
        assertEquals(true, result.isNextPageAvailable)
    }

    @Test
    fun `getGame return failed`() = runTest {
        val response = """
            {"error": "error message"}
        """.trimIndent()
        coEvery {
            gameApi.getGames(any(), any(), any())
        } returns Response.error(401, response.toResponseBody("application/json".toMediaType()))

        val result = gameRepository.getGames(null, null)

        coVerify {
            gameApi.getGames(null, null, 20)
        }

        assert(result is Result.Error)
        assertEquals(
            "error : error message",
            (result as Result.Error).error.message
        )
    }

    @Test
    fun `getGame throw Exception`() = runTest {
        coEvery {
            gameApi.getGames(any(), any(), any())
        } answers {
            throw Exception("error")
        }

        val result = gameRepository.getGames(null, null)

        coVerify {
            gameApi.getGames(null, null, 20)
        }

        assert(result is Result.Error)
        assertEquals("error", (result as Result.Error).error.message)
    }

    @Test
    fun `getGameDetail return success`() = runTest {
        val response = com.example.data.dto.GameDetail(
            id = 1
        )
        coEvery {
            gameApi.getDetail(any(), any())
        } returns Response.success(response)

        val result = gameRepository.getGameDetail(1)

        coVerify {
            gameApi.getDetail(1)
        }

        assert(result is Result.Success)
        assertEquals(
            Result.Success(com.example.data.dto.GameDetail(id = 1)).data,
            (result as Result.Success).data
        )
    }

    @Test
    fun `getGameDetail return failed`() = runTest {
        val response = """
            {"error": "error message"}
        """.trimIndent()
        coEvery {
            gameApi.getDetail(any(), any())
        } returns Response.error(401, response.toResponseBody("application/json".toMediaType()))

        val result = gameRepository.getGameDetail(1)

        coVerify {
            gameApi.getDetail(1)
        }

        assert(result is Result.Error)
        assertEquals(
            "error : error message",
            (result as Result.Error).error.message
        )
    }

    @Test
    fun `getGameDetail throw Exception`() = runTest {
        coEvery {
            gameApi.getDetail(any(), any())
        } answers {
            throw Exception("error")
        }

        val result = gameRepository.getGameDetail(1)

        coVerify {
            gameApi.getDetail(1)
        }

        assert(result is Result.Error)
        assertEquals("error", (result as Result.Error).error.message)
    }
}