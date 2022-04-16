package br.com.mirespeiti.calendarfill.calendar.domain

import java.util.*
//Todo = transformar em listas
data class Calendario(
    val date: Date,
    val work: Boolean,
    val event: String?,
) : CalendarioItem {
    override fun dayOn() = date
    override fun work() = work
    override fun event() = event

    override fun toString(): String {
        return "Work: $work, Event: $event, Day: ${date.toGMTString()} "
    }
}


