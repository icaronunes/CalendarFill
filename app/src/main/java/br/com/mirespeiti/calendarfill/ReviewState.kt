package br.com.mirespeiti.calendarfill

import br.com.mirespeiti.calendarfill.calendardiario.domain.CalendarioItem

data class ReviewState(
    @Suppress("ArrayInDataClass") val reviewsList: Array<CalendarioItem> = emptyArray(),
    var loading: Boolean = false,
    var error: Throwable? = null
)
