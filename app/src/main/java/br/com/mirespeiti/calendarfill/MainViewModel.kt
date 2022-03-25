package br.com.mirespeiti.calendarfill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mirespeiti.calendarfill.calendardiario.domain.Calendario
import br.com.mirespeiti.calendarfill.calendardiario.domain.CalendarioItem
import br.com.mirespeiti.calendarfill.domain.MapperMovie
import br.com.mirespeiti.calendarfill.domain.Resource
import br.com.mirespeiti.calendarfill.usecase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    fun getReviewsOnMonth(month: Calendar, fake: Boolean = false) {
        viewModelScope.launch {
            useCaseWeather(month).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val mapList = when(fake) {
                            false -> resource.data.results?.map {
                                it.mapTo(mapper)
                            }?.toTypedArray<CalendarioItem>() ?: emptyArray()
                            true -> fakeDates(month)
                        }
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

    fun fakeDates(it: Calendar): Array<CalendarioItem> {
        val colors: MutableList<CalendarioItem> = mutableListOf()
        (1..25).forEach { number ->
            val c = Calendar.getInstance()
            c.time = it.time
            c.set(Calendar.DATE, number)
            if(number !in 15..20) {
                val date = c.time
                val item = Calendario(
                    date = date,
                    work = number % 5 == 0,
                    event = if (number == 24) "nada" else null
                )
                colors.add(item)
            }
        }
        return colors.toTypedArray()
    }
}