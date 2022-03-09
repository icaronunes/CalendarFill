package br.com.mirespeiti.calendarfill.calendardiario

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.mirespeiti.calendarfill.R
import br.com.mirespeiti.calendarfill.calendardiario.adapter.CalendarDiarioAdapter
import br.com.mirespeiti.calendarfill.calendardiario.adapter.makeToast
import br.com.mirespeiti.calendarfill.databinding.CalendarFillLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarFillView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs), CalendarDiarioInterface {
    companion object {
        private const val TOTALDAYS = 42
        private const val FORMAT_MONTH_YEAR = "MMMM yyyy"
        private const val FORMAT_WEEK = "EEEEE"
    }

    private var bind = CalendarFillLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var monthCurent: Calendar = GregorianCalendar.getInstance()
    private lateinit var activity: AppCompatActivity
    var onclick: (Date, CalendarioItem?) -> Unit = { date: Date, _: CalendarioItem? ->
        activity.makeToast(date.toGMTString())
    }
    var callDataColors: (Calendar) -> Array<CalendarioItem> = {
        emptyArray()
    }
    private var dateSelected: Date = Date()

    init {
        initViews(attrs, defStyleAttr, defStyleRes)
        fillWeekDayLabel()
        bind.forward.setOnClickListener {
            monthCurent.add(Calendar.MONTH, 1)
            val colors = callDataColors(monthCurent)
            updateDates(datesColors = colors, selectedMoth = monthCurent)
        }

        bind.back.setOnClickListener {
            monthCurent.add(Calendar.MONTH, -1)
            val colors = callDataColors(monthCurent)
            updateDates(datesColors = colors, selectedMoth = monthCurent)
        }
    }

    private fun fillWeekDayLabel() {
        val aux: Calendar = Calendar.getInstance()
        var account = 0
        do {
             val week = SimpleDateFormat(FORMAT_WEEK, Locale.getDefault()).format(aux.time)
             when (aux[Calendar.DAY_OF_WEEK]) {
                Calendar.SUNDAY -> { bind.labelSunday.text = week }
                Calendar.MONDAY -> { bind.labelMonday.text = week  }
                Calendar.TUESDAY -> { bind.labelTuesday.text = week }
                Calendar.WEDNESDAY -> {bind.labelWednesday.text = week }
                Calendar.THURSDAY -> { bind.labelThursday.text = week }
                Calendar.FRIDAY -> { bind.labelFriday.text = week }
                Calendar.SATURDAY-> { bind.labelSaturday.text = week }
             }
            ++account
            aux.set(Calendar.DATE, account)
        } while (account <= 7)
    }

    private fun initViews(attrs: AttributeSet?, defStyle: Int, defStyleRes: Int) {
        attrs ?: return
        val attributeSet = context.obtainStyledAttributes(
            attrs,
            R.styleable.CalendarDiarioView,
            defStyle,
            defStyleRes
        )
        with(attributeSet) {
            try {
            } finally {
                recycle()
            }
        }

        updateDates()
    }

    fun setupToolbarCalendar(
        activity: AppCompatActivity,
        initMonth: Calendar,
    ) {
        monthCurent = initMonth
        dateSelected = initMonth.time
        this.activity = activity
    }

    private fun updateDescription(calendar: Calendar) {
        bind.monthYear.text = SimpleDateFormat(FORMAT_MONTH_YEAR, Locale.getDefault())
            .format(calendar.time)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    private fun updateDates(
        datesColors: Array<CalendarioItem> = arrayOf(),
        selectedMoth: Calendar = monthCurent
    ) {
        updateDescription(selectedMoth.clone() as Calendar)
        val dayOnGrid: MutableList<Date> = fillDaysOnVisible(selectedMoth)
        Log.d("Calendar", "data selecionada ${dateSelected.toGMTString()}")
        bind.gridCalendar.adapter =
            CalendarDiarioAdapter(this, dayOnGrid, datesColors, dateSelected, selectedMoth)
        bind.gridCalendar.setOnItemClickListener { parent, _, position, _ ->
            val calendarItemSelected =
                getCalendarItem(informations = datesColors, date = dateSelected)
            dateSelected = parent.getItemAtPosition(position) as Date
            onclick(dateSelected, calendarItemSelected)
            bind.gridCalendar.adapter =
                CalendarDiarioAdapter(
                    this,
                    dayOnGrid,
                    datesColors,
                    dateSelected,
                    currentMonth = selectedMoth
                )
        }
    }

    private fun fillDaysOnVisible(selectedMonth: Calendar): MutableList<Date> {
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

    private fun validDateOn(dates: Array<CalendarioItem>): Boolean {
        return getCalendarItem(dates, dateSelected) == null
    }

    override fun getContextDiario(): Context = context
    override fun getCalendarItem(informations: Array<CalendarioItem>, date: Date): CalendarioItem? {
        return informations.firstOrNull { info -> date.isSame(info.dayOn()) }
    }
}