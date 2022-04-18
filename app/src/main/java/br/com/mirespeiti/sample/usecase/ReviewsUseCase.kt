package br.com.mirespeiti.sample.usecase

import androidx.lifecycle.SavedStateHandle
import br.com.mirespeiti.sample.domain.Resource
import kotlinx.coroutines.flow.flow
import repository.ReviewsRepository
import java.util.*
import javax.inject.Inject

class ReviewsUseCase @Inject constructor(
    private val repository: ReviewsRepository,
    saveHandle: SavedStateHandle
) {

    companion object {
        const val MONTH = "month"
    }

    init {
        saveHandle.get<Calendar>(MONTH)?.let {
            invoke(it)
        }
    }

    operator fun invoke(month: Calendar?) = flow {
        try {
            emit(Resource.Loading(true))
            val response = repository.getReviewsNYTime(month)
            emit(Resource.Success(response))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}