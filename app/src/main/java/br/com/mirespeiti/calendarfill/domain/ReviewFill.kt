package br.com.mirespeiti.calendarfill.domain

import br.com.mirespeiti.calendarfill.calendar.domain.CalendarioItem
import java.util.*

data class ReviewFill(
    val event: String?,
    val work: Boolean,
    val dayOn: Date
) : CalendarioItem {
    override fun dayOn(): Date = dayOn
    override fun work(): Boolean = work
    override fun event(): String? = event
}
