package br.com.mirespeiti.calendarfill.calendar.ext

import java.util.*

fun Date.isSame(day: Date?) : Boolean {
    if(day == null) return false
    return date == day.date && month == day.month && year == day.year
}

fun Date.isNotSame(day: Date?) : Boolean {
    if(day == null) return true
    return !(date == day.date && month == day.month && year == day.year)
}

fun Date.isSameMonth(date: Date?): Boolean {
    if(date == null) return false
    return month == date.month && year == date.year
}

fun Date.otheMonth(day: Date?) = !isSameMonth(day)