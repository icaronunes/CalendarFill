package br.com.mirespeiti.calendarfill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mirespeiti.calendarfill.calendardiario.domain.CalendarioItem
import br.com.mirespeiti.calendarfill.domain.MapperMovie
import br.com.mirespeiti.calendarfill.domain.MapperToDTO
import br.com.mirespeiti.calendarfill.domain.Resource
import br.com.mirespeiti.calendarfill.domain.ReviewFill
import br.com.mirespeiti.calendarfill.usecase.WeatherUseCase
import br.com.mirespeiti.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseWeather: WeatherUseCase,
    private val mapper: MapperMovie
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewState())
    val state: StateFlow<ReviewState> = _state.asStateFlow()

    fun getReviewsOnMonth(month: Calendar) {
        viewModelScope.launch {
            useCaseWeather.execute(month).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val mapList = resource.data.results?.map {
                            it.mapTo(mapper)
                        }?.toTypedArray<CalendarioItem>() ?: emptyArray()

                        _state.update {
                            it.copy(
                                reviewsList = mapList,
                                error = null,
                                loading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update { state ->
                            state.error = resource.error
                            state
                        }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(loading = resource.status) }
                    }
                }
            }
        }
    }

}