package br.com.mirespeiti.calendarfill.calendardiario

import android.content.Context
import br.com.mirespeiti.calendarfill.calendardiario.domain.CalendarioItem
import java.util.*

/**
 * TODO: document your custom view class.
 */

interface CalendarDiarioInterface {
    fun getContextDiario(): Context
    fun getCalendarItem(informations: Array<CalendarioItem>, date: Date): CalendarioItem?
}