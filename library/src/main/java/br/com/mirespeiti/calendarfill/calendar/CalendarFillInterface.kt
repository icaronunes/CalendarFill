package br.com.mirespeiti.calendarfill.calendar

import android.content.Context
import br.com.mirespeiti.calendarfill.calendar.domain.CalendarioItem
import java.util.*

/**
 * TODO: document your custom view class.
 */

interface CalendarFillInterface {
    fun getContextDiario(): Context
    fun getCalendarItem(informations: Array<CalendarioItem>, date: Date): CalendarioItem?
}