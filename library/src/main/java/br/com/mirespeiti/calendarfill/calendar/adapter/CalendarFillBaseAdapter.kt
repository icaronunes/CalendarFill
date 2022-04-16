package br.com.mirespeiti.calendarfill.calendar.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import br.com.mirespeiti.calendarfill.R
import br.com.mirespeiti.calendarfill.calendar.CalendarFillInterface
import br.com.mirespeiti.calendarfill.calendar.CalendarFillView
import br.com.mirespeiti.calendarfill.calendar.domain.CalendarioItem
import br.com.mirespeiti.calendarfill.calendar.domain.ColorFill
import br.com.mirespeiti.calendarfill.calendar.domain.TypeBackground
import br.com.mirespeiti.calendarfill.calendar.domain.eventDay
import br.com.mirespeiti.calendarfill.calendar.ext.isNotSame
import br.com.mirespeiti.calendarfill.databinding.ItemCalendarAdapterBinding
import java.text.SimpleDateFormat
import java.util.*


abstract class CalendarFillBaseAdapter(
    calendarFillContext: CalendarFillView,
    open var colors: ColorFill = object : ColorFill() {},
    open var dateColors: Array<CalendarioItem> = emptyArray(),
    var currentMonth: Calendar,
    var dates: List<Date> = calendarFillContext.fillDaysOnVisible(currentMonth),
    @LayoutRes layout: Int = R.layout.item_calendar_adapter,
) : ArrayAdapter<Date>(calendarFillContext.context, layout, dates), CalendarFillInterface {

    private val inflaterLayout: LayoutInflater by lazy { LayoutInflater.from(context) }
    private val DAYFORMAT = "dd"
    val EMPTY = ""
    private val TOTALDAYS = 42

    open val startBackground: Int = R.drawable.background_calendar_start
    open var endBackground: Int = R.drawable.background_calendar_end
    open var fullBackground: Int = R.drawable.background_calendar_full
    open var aroundBackground: Int = R.drawable.background_calendar_around

    abstract var selectedDay: Date
    abstract fun customColorsDates(container: ConstraintLayout, item: TextView, date: Date)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val date = getItem(position)
        if (convertView == null)
            view = ItemCalendarAdapterBinding.inflate(inflaterLayout, parent, false).apply {
                SimpleDateFormat(DAYFORMAT, Locale.getDefault()).format(date).let {
                    txtDate.text = it
                }

                customColorsDates(calendarAdapterContainer, txtDate, date)
            }.root
        return view ?: View(context)
    }

    override fun getItem(position: Int) = dates[position]

    fun fillDaysOnVisible(selectedMonth: Calendar): MutableList<Date> {
        val listDays: MutableList<Date> = mutableListOf()
        val aux = backInitWeek(selectedMonth)
        do { //Cria lista com 42 posicoes
            listDays.add(aux.time)
            aux.add(Calendar.DAY_OF_MONTH, 1)
        } while (listDays.size < TOTALDAYS)
        return listDays
    }

    private fun backInitWeek(currentMonth: Calendar): Calendar {
        val result = Calendar.getInstance()
        result.time = currentMonth.time
        //Vai até dia 1 do mes
        result[Calendar.DAY_OF_MONTH] = 1
        //pega primeiro dia da semana do dia 1
        val firstDayWeek: Int = result.get(Calendar.DAY_OF_WEEK) - 1
        //retorna do dia 1 até o começa da semana
        result.add(Calendar.DAY_OF_MONTH, -firstDayWeek)
        return result
    }


    open fun primeryColor(
        container: ConstraintLayout,
        number: TextView,
        prev: CalendarioItem?,
        next: CalendarioItem?,
    ) {
        val back = prev != null && prev.eventDay() && selectedDay.isNotSame(prev.dayOn())
        val forward = next != null && next.eventDay() && selectedDay.isNotSame(next.dayOn())
        when {
            back && forward -> fillColor(
                color = colors.primary,
                view = container,
                type = TypeBackground.FULL
            )
            !back && forward -> fillColor(
                color = colors.primary,
                view = container,
                type = TypeBackground.START
            )
            back && !forward -> fillColor(
                color = colors.primary,
                view = container,
                type = TypeBackground.END
            )
            !back && !forward -> fillColor(
                color = colors.primary,
                view = number,
                type = TypeBackground.AROUND
            )
        }
    }

    open fun secondColor(
        container: ConstraintLayout,
        text: TextView,
        prev: CalendarioItem?,
        next: CalendarioItem?,
    ) {
        val back = prev != null && prev.work() && selectedDay.isNotSame(prev.dayOn())
        val forward = next != null && next.work() && selectedDay.isNotSame(next.dayOn())

        when {
            back && forward -> fillColor(
                color = colors.secondary,
                view = container,
                type = TypeBackground.FULL
            )
            !back && forward -> fillColor(
                color = colors.secondary,
                view = container,
                type = TypeBackground.START
            )
            back && !forward -> fillColor(
                color = colors.secondary,
                view = container,
                type = TypeBackground.END
            )
            !back && !forward -> fillColor(
                color = colors.secondary,
                view = text,
                type = TypeBackground.AROUND
            )
        }
    }

    open fun fillColor(@ColorRes color: Int, view: View, type: TypeBackground) {
        val typeBackground = when (type) {
            TypeBackground.START -> startBackground
            TypeBackground.END -> endBackground
            TypeBackground.FULL -> fullBackground
            TypeBackground.AROUND -> aroundBackground
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

}
