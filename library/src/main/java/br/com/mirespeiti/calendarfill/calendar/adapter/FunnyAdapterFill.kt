package br.com.mirespeiti.calendarfill.calendar.adapter

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import br.com.mirespeiti.calendarfill.calendar.CalendarFillInterface
import br.com.mirespeiti.calendarfill.calendar.CalendarFillView
import br.com.mirespeiti.calendarfill.calendar.domain.ColorFill
import br.com.mirespeiti.calendarfill.calendar.domain.TypeBackground
import java.util.*

class FunnyAdapterFill(
    calendarFillContext: CalendarFillView,
    override var selectedDay: Date = Date(),
    override var colors: ColorFill = object : ColorFill() {
        override var selected: Int = android.R.color.holo_red_dark
        override var default: Int = android.R.color.holo_green_dark
        override var secondary: Int = android.R.color.holo_purple
    },
) : CalendarFillBaseAdapter(
    calendarFillContext = calendarFillContext,
    currentMonth = Calendar.getInstance()
), CalendarFillInterface by calendarFillContext {

    override fun customColorsDates(container: ConstraintLayout, item: TextView, date: Date) {

        when (date.day % 3) {
            0 -> container.setBackgroundColor(
                ResourcesCompat.getColor(
                    context.resources, colors.secondary, null
                )
            )
            1 -> container.setBackgroundColor(
                ResourcesCompat.getColor(
                    context.resources, colors.default, null
                ))
            else -> setupSelectedBackground(item)
        }
    }

    private fun setupSelectedBackground(item: TextView) {
        fillColor(color = colors.selected, view = item, type = TypeBackground.AROUND)
    }
}