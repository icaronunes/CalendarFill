package br.com.mirespeiti.sample

import br.com.mirespeiti.calendarfill.calendar.domain.CalendarioItem

data class ReviewState(
    @Suppress("ArrayInDataClass") val reviewsList: Array<CalendarioItem> = emptyArray(),
    var loading: Boolean = false,
    var error: Throwable? = null
)
