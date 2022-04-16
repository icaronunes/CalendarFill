package br.com.mirespeiti.calendarfill.calendar.domain

import br.com.mirespeiti.calendarfill.R

abstract class ColorFill {
    open var primary: Int = R.color.light_blue_600
    open var secondary: Int = R.color.green_20C7A3
    open var selected: Int = R.color.yellow_F7B100
    open var default: Int = R.color.gray_600
    open var other: Int = R.color.gray_400
}