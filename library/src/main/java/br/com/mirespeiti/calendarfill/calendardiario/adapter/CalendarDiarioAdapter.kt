package br.com.mirespeiti.calendarfill.calendardiario.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import br.com.mirespeiti.calendarfill.R
import br.com.mirespeiti.calendarfill.calendardiario.CalendarDiarioInterface
import br.com.mirespeiti.calendarfill.calendardiario.domain.*
import br.com.mirespeiti.calendarfill.calendardiario.ext.ifValid
import br.com.mirespeiti.calendarfill.calendardiario.ext.isNotSame
import br.com.mirespeiti.calendarfill.calendardiario.ext.isSame
import br.com.mirespeiti.calendarfill.calendardiario.ext.otheMonth
import br.com.mirespeiti.calendarfill.databinding.ItemCalendarAdapterBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarDiarioAdapter(
    private val wrapInterface: CalendarDiarioInterface,
    private val dates: List<Date>,
    private val dateColors: Array<CalendarioItem>,
    private var selectedDay: Date,
    private val currentMonth: Calendar,
    val colors: ColorFill,
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

    private fun Date.dateOnInformation(days: Int): Date {
        return Calendar.getInstance().apply {
            time = this@dateOnInformation
            add(Calendar.DATE, days)
        }.time
    }

    private fun setupViewByInfo(date: Date, container: ConstraintLayout, textView: TextView) {
        val info = wrapInterface.getCalendarItem(dateColors, date)

        val prev: CalendarioItem? = date.dateOnInformation(-1).getInformation(dateColors)
        val next: CalendarioItem? = date.dateOnInformation(1).getInformation(dateColors)
        when {
            date.isSame(selectedDay) -> setupSelectedBackground(textView)
            info == null -> {
                if (date.otheMonth(currentMonth.time)) {
                    fillColorBackground(
                        color = colors.default,
                        view = container,
                        type = TypeBackground.FULL
                    )
                    textView.setTextColor(
                        ResourcesCompat.getColor(
                            context.resources,
                            android.R.color.white,
                            null
                        )
                    )
                } else dayOff(container, textView)
            }
            info.isEmpty() -> dayOff(container, textView)
            info.eventDay() -> dayCurrentEvent(container, textView, prev, next)
            info.work() -> workDay(container, textView, prev, next)
            else -> textView.text = EMPTY
        }
    }

    private fun fillColorBackground(@ColorRes color: Int, view: View, type: TypeBackground) {
        val typeBackground = when (type) {
            TypeBackground.START -> R.drawable.background_calendar_start
            TypeBackground.END -> R.drawable.background_calendar_end
            TypeBackground.FULL -> R.drawable.background_calendar_full
            TypeBackground.AROUND -> R.drawable.background_calendar_around
        }
        AppCompatResources.getDrawable(context, typeBackground)?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(
                wrappedDrawable,
                ResourcesCompat.getColor(context.resources, color, null)
            )
            view.background = wrappedDrawable
        }
    }

    private fun setupView(container: ConstraintLayout, item: TextView, date: Date) {
        setupViewByInfo(date, container, item)
    }

    private fun setupSelectedBackground(item: TextView) {
        fillColorBackground(color = colors.selected, view = item, type = TypeBackground.AROUND)
    }

    private fun dayOff(
        container: ConstraintLayout,
        item: TextView,
    ) {
        fillColorBackground(
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

    private fun dayCurrentEvent(
        container: ConstraintLayout,
        number: TextView,
        prev: CalendarioItem?,
        next: CalendarioItem?
    ) {
        val back = prev != null && prev.eventDay() && selectedDay.isNotSame(prev.dayOn())
        val forward = next != null && next.eventDay() && selectedDay.isNotSame(next.dayOn())
        when {
            back && forward -> fillColorBackground(
                color = colors.primary,
                view = container,
                type = TypeBackground.FULL
            )
            !back && forward -> fillColorBackground(
                color = colors.primary,
                view = container,
                type = TypeBackground.START
            )
            back && !forward -> fillColorBackground(
                color = colors.primary,
                view = container,
                type = TypeBackground.END
            )
            !back && !forward -> fillColorBackground(
                color = colors.primary,
                view = number,
                type = TypeBackground.AROUND
            )
        }
    }

    private fun workDay(
        container: ConstraintLayout,
        text: TextView,
        prev: CalendarioItem?,
        next: CalendarioItem?,
    ) {
        val back = prev != null && prev.work() && selectedDay.isNotSame(prev.dayOn())
        val forward = next != null && next.work() && selectedDay.isNotSame(next.dayOn())

        when {
            back && forward -> fillColorBackground(
                color = colors.secondary,
                view = container,
                type = TypeBackground.FULL
            )
            !back && forward -> fillColorBackground(
                color = colors.secondary,
                view = container,
                type = TypeBackground.START
            )
            back && !forward -> fillColorBackground(
                color = colors.secondary,
                view = container,
                type = TypeBackground.END
            )
            !back && !forward -> fillColorBackground(
                color = colors.secondary,
                view = text,
                type = TypeBackground.AROUND
            )
        }
    }

    private fun Date.getInformation(informations: Array<CalendarioItem>): CalendarioItem? {
        val position = informations.indexOfFirst { isSame(it.dayOn()) }
        if (position == -1) return null
        return informations.getOrNull(position)
    }

    override fun getItem(position: Int) = dates[position]
}

