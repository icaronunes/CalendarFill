package br.com.mirespeiti.calendarfill.calendardiario.adapter

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import br.com.mirespeiti.calendarfill.R
import br.com.mirespeiti.calendarfill.calendardiario.CalendarFillInterface
import br.com.mirespeiti.calendarfill.calendardiario.CalendarFillView
import br.com.mirespeiti.calendarfill.calendardiario.domain.*
import br.com.mirespeiti.calendarfill.calendardiario.ext.isSame
import br.com.mirespeiti.calendarfill.calendardiario.ext.otheMonth
import java.util.*

class CalendarFillAdapter(
    calendarFillContext: CalendarFillView,
    override var selectedDay: Date,
    dates: List<Date>,
    override var dateColors: Array<CalendarioItem>,
    val month: Calendar,
    override var colors: ColorFill,
) : CalendarFillBaseAdapter(
    calendarFillContext = calendarFillContext,
    dateColors = dateColors,
    currentMonth = month,
    colors = colors,
    dates = dates,
), CalendarFillInterface by calendarFillContext {

    private fun Date.dateOnInformation(days: Int): Date {
        return Calendar.getInstance().apply {
            time = this@dateOnInformation
            add(Calendar.DATE, days)
        }.time
    }

    override fun customColorsDates(container: ConstraintLayout, item: TextView, date: Date) {
        val info = getCalendarItem(dateColors, date)

        val prev: CalendarioItem? = date.dateOnInformation(-1).getInformation(dateColors)
        val next: CalendarioItem? = date.dateOnInformation(1).getInformation(dateColors)
        when {
            date.isSame(selectedDay) -> setupSelectedBackground(item)
            info == null -> {
                if (date.otheMonth(currentMonth.time)) {
                    fillColor(
                        color = colors.default,
                        view = container,
                        type = TypeBackground.FULL
                    )
                    item.setTextColor(
                        ResourcesCompat.getColor(
                            context.resources,
                            android.R.color.white,
                            null
                        )
                    )
                } else dayOff(container, item)
            }
            info.isEmpty() -> dayOff(container, item)
            info.eventDay() -> primeryColor(container, item, prev, next)
            info.work() -> secondColor(container, item, prev, next)
            else -> item.text = super.EMPTY
        }
    }


    private fun setupSelectedBackground(item: TextView) {
        fillColor(color = colors.selected, view = item, type = TypeBackground.AROUND)
    }

    private fun dayOff(
        container: ConstraintLayout,
        item: TextView,
    ) {
        fillColor(
            color = android.R.color.transparent,
            view = container,
            type = TypeBackground.FULL
        )

        item.setTextColor(
            ResourcesCompat.getColor(
                context.resources,
                colors.default,
                null
            )
        )
    }

    private fun Date.getInformation(informations: Array<CalendarioItem>): CalendarioItem? {
        val position = informations.indexOfFirst { isSame(it.dayOn()) }
        if (position == -1) return null
        return informations.getOrNull(position)
    }
}

