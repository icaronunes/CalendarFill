package br.com.mirespeiti.calendarfill.usecase

import br.com.mirespeiti.calendarfill.MainCoroutineScopeRule
import br.com.mirespeiti.calendarfill.domain.Resource
import br.com.mirespeiti.data.ResponseReview
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class WeatherUseCaseTest {

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val mockUseCase = mockk<ReviewsUseCase>()
    private val result = ResponseReview(
        status = "ok",
        copyright = "",
        hasMore = false,
        numResults = 0,
        results = listOf()
    )

    @Test
    fun `success_on_Reviews_api`() = runTest {

        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(result))
            emit(Resource.Loading(false))
        }

        coEvery { mockUseCase(any()) } returns flow
        val job = launch {
            mockUseCase(any()).collectIndexed { index, value ->
                when (index) {
                    0 -> assertThat(value).isEqualTo(Resource.Loading(true))
                    1 -> assertThat(value).isEqualTo(Resource.Success(result))
                    2 -> assertThat(value).isEqualTo(Resource.Loading(false))
                }
            }
        }

        mockUseCase(any())
        coVerify { mockUseCase(any()) }

        job.cancel()
    }

    @Test
    fun `error_on_Reviews_api`() = runTest {
        val error = Throwable( "error")
        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Error(error))
            emit(Resource.Loading(false))
        }

        coEvery { mockUseCase(any()) } returns flow
        val job = launch {
            mockUseCase(any()).collectIndexed { index, value ->
                when (index) {
                    0 -> assertThat(value).isEqualTo(Resource.Loading(true))
                    1 -> assertThat(value).isEqualTo(Resource.Error(error))
                    2 -> assertThat(value).isEqualTo(Resource.Loading(false))
                }
            }
        }

        mockUseCase(any())
        coVerify { mockUseCase(any()) }

        job.cancel()
    }


}