package br.com.mirespeiti.calendarfill.calendardiario

import br.com.mirespeiti.calendarfill.R

interface CalendarConfig {
	fun backgroundTodo() = R.drawable.background_calendar_blue
	fun backgroundTodoInit() = R.drawable.background_calendar_blue_init
	fun backgroundTodoEnd() = R.drawable.background_calendar_blue_end
	fun backgroundTodoAround() = R.drawable.background_calendar_blue_around
	fun backgroundDone() = R.drawable.background_calendar_green
	fun backgroundDoneInit() = R.drawable.background_calendar_green_init
	fun backgroundDoneEnd() = R.drawable.background_calendar_green_end
	fun backgroundDoneAround() = R.drawable.background_calendar_green_around
	fun backgroundBackground() = R.drawable.background_calendar_gray
	fun selectedDayColor() = R.drawable.background_calendar_yellow_around
}