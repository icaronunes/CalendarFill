package br.com.mirespeiti.calendarfill.calendardiario.adapter

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import br.com.mirespeiti.calendarfill.R
import br.com.mirespeiti.calendarfill.calendardiario.CalendarFillInterface
import br.com.mirespeiti.calendarfill.calendardiario.CalendarFillView
import br.com.mirespeiti.calendarfill.calendardiario.domain.TypeBackground
import java.util.*

class SecondCustomAdapterFill(
    calendarFillContext: CalendarFillView,
    override var selectedDay: Date = Date(),
) : CalendarFillBaseAdapter(
    calendarFillContext = calendarFillContext,
    currentMonth = Calendar.getInstance()
), CalendarFillInterface by calendarFillContext {

    override fun customColorsDates(container: ConstraintLayout, item: TextView, date: Date) {

        when(date.day % 3) {
            0 -> container.setBackgroundColor(
                ResourcesCompat.getColor(
                    context.resources, R.color.light_blue_600, null
                )
            )
            1 -> container.setBackgroundColor(
                ResourcesCompat.getColor(
                    context.resources, R.color.gray_C1C9D8, null
                ))
            else -> setupSelectedBackground(item)
        }
    }

    private fun setupSelectedBackground(item: TextView) {
        fillColor(color = colors.selected, view = item, type = TypeBackground.AROUND)
    }
}