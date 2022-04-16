package br.com.mirespeiti.calendarfill.calendar.domain

import java.util.Date

fun CalendarioItem.isEmpty() = !this.work() && event() == null
fun CalendarioItem.eventDay() = !this.work() && event() != null
fun CalendarioItem.workDayNotEvent() = this.work() && event() == null

interface CalendarioItem {
	fun dayOn(): Date
	fun work(): Boolean
	fun event(): String?
}
