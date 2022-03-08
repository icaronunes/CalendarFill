package br.com.mirespeiti.calendarfill.calendardiario.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import br.com.mirespeiti.calendarfill.R
import br.com.mirespeiti.calendarfill.calendardiario.*
import br.com.mirespeiti.calendarfill.databinding.ItemCalendarAdapterBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarDiarioAdapter(
    private val wrapInterface: CalendarDiarioInterface,
    private val dates: List<Date>,
    private val dateColors: Array<CalendarioItem>,
    private var selectedDay: Date,
    private val currentMonth: Calendar,
    var setup: CalendarConfig = object : CalendarConfig {},
) : ArrayAdapter<Date>(wrapInterface.getContextDiario(), R.layout.item_calendar_adapter, dates) {
    private val inflaterLayout: LayoutInflater by lazy { LayoutInflater.from(context) }
    private val DAYFORMAT = "dd"
    private val EMPTY = ""

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val date = getItem(position)
        if (convertView == null)
            view = ItemCalendarAdapterBinding.inflate(inflaterLayout, parent, false).apply {
                SimpleDateFormat(DAYFORMAT, Locale.getDefault()).format(date).let {
                    txtDate.text = it
                }

                dateColors.ifValid { setupView(calendarAdapterContainer, txtDate, date) }
            }.root
        return view ?: View(context)
    }

    private fun setupView(container: ConstraintLayout, item: TextView, date: Date) {
        setupViewByInfo(date, container, item)
    }

    private fun setupMonthBackground(container: ConstraintLayout) {
        ResourcesCompat.getDrawable(context.resources, setup.backgroundBackground(), null)?.let {
            container.background = it
        }
    }

    private fun setupSelectedBackground(item: TextView) {
        ResourcesCompat.getDrawable(context.resources, setup.selectedDayColor(), null)?.let {
            item.background = it
        }
    }

    private fun dayOff(
        container: ConstraintLayout,
        item: TextView,
    ) {
        container.setBackgroundColor(
            ResourcesCompat.getColor(
                context.resources,
                android.R.color.transparent,
                null
            )
        )
        item.setTextColor(
            ResourcesCompat.getColor(
                context.resources,
                R.color.light_blue_600,
                null
            )
        )
    }

    private fun setupViewByInfo(date: Date, container: ConstraintLayout, item: TextView) {
        val info = wrapInterface.getCalendarItem(dateColors, date)
        val prev: CalendarioItem? = date.informationPrev(dateColors) //usar wrapinterface como ext
        val next: CalendarioItem? = date.informationNext(dateColors)
        when {
            date.isSame(selectedDay) -> setupSelectedBackground(item)
            info == null -> {
                if (date.otheMonth(currentMonth.time)) {
                    setupMonthBackground(container)
                } else {
                    dayOff(container, item)
                }
            }
            info.isEmpty() -> dayOff(container, item)
            info.eventDay() -> {
                dayCurrentEvent(container, item, prev, next)
            } // Azul
            info.work() -> eventDay(container, item, prev, next) // verde
            else -> item.text = EMPTY
        }
        Log.d("Calendar", "updateDates: ${date.toGMTString()} -- ${info.toString()}")
    }

    private fun dayCurrentEvent(
        container: ConstraintLayout,
        item: TextView,
        prev: CalendarioItem?,
        next: CalendarioItem?
    ) {
        val back = prev?.eventDay() == true && !selectedDay.isSame(prev.dayOn())
        val forward = next?.eventDay() == true && !selectedDay.isSame(next.dayOn())
        when {
            back && forward -> container.background = getDrawable(setup.backgroundTodo())
            !back && forward -> container.background = getDrawable(setup.backgroundTodoInit())
            back && !forward -> container.background = getDrawable(setup.backgroundTodoEnd())
            !back && !forward -> item.background = getDrawable(setup.backgroundTodoAround())
        }
    }

    private fun eventDay(
        container: ConstraintLayout,
        item: TextView,
        prev: CalendarioItem?,
        next: CalendarioItem?,
    ) {
        val back = prev?.work() ?: false && !selectedDay.isSame(prev?.dayOn())
        val forward = next?.work() ?: false && !selectedDay.isSame(next?.dayOn())

        when {
            back && forward -> container.background = getDrawable(setup.backgroundDone())
            !back && forward -> container.background = getDrawable(setup.backgroundDoneInit())
            back && !forward -> container.background = getDrawable(setup.backgroundDoneEnd())
            !back && !forward -> item.background = getDrawable(setup.backgroundDoneAround())
        }
    }

    private fun Date.informationPrev(informations: Array<CalendarioItem>): CalendarioItem? {
        val position = informations.indexOfFirst { this.isSame(it.dayOn()) }
        return informations.getOrNull(position - 1)
    }

    private fun Date.informationNext(informations: Array<CalendarioItem>): CalendarioItem? {
        val position = informations.indexOfFirst { this.isSame(it.dayOn()) }
        return informations.getOrNull(position + 1)
    }

    private fun getDrawable(drawable: Int) = ResourcesCompat.getDrawable(context.resources, drawable, null)
    override fun getItem(position: Int) = dates[position]
}

