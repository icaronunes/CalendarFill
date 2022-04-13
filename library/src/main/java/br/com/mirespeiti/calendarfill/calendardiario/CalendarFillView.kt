package br.com.mirespeiti.calendarfill.calendardiario

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.mirespeiti.calendarfill.R
import br.com.mirespeiti.calendarfill.calendardiario.adapter.CalendarFillAdapter
import br.com.mirespeiti.calendarfill.calendardiario.adapter.CalendarFillBaseAdapter
import br.com.mirespeiti.calendarfill.calendardiario.domain.CalendarioItem
import br.com.mirespeiti.calendarfill.calendardiario.domain.ColorFill
import br.com.mirespeiti.calendarfill.calendardiario.ext.isSame
import br.com.mirespeiti.calendarfill.calendardiario.ext.makeToast
import br.com.mirespeiti.calendarfill.databinding.CalendarFillLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarFillView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs), CalendarFillInterface {
    companion object {
        private const val TOTALDAYS = 42
        private const val FORMAT_MONTH_YEAR = "MMMM yyyy"
        private const val FORMAT_WEEK = "EEEEE"
    }

    enum class CallBack { BOTH, WITH_RETURN, NOT_RETURN }

    private var dateSelected: Date = Date()
    private var typeCallBack: CallBack = CallBack.WITH_RETURN
    private var monthCurent: Calendar = GregorianCalendar.getInstance()
    private var colors: ColorFill = object : ColorFill() {}
    private var defaultAdapter: CalendarFillBaseAdapter = CalendarFillAdapter(
        calendarFillContext = this,
        dateColors = emptyArray(),
        dates = fillDaysOnVisible(monthCurent),
        selectedDay = dateSelected,
        month = monthCurent,
        colors = colors
    )

    private var bind = CalendarFillLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var onclick: (Date, CalendarioItem?) -> Unit = { date: Date, _: CalendarioItem? ->
        context.makeToast(date.toGMTString())
    }
    var callUpdates: (Calendar) -> Array<CalendarioItem> = { emptyArray() }
    var callDate: (Calendar) -> Unit = { }

    init {
        initViews(attrs, defStyleAttr, defStyleRes)
        fillWeekDayLabel()

        bind.forward.setOnClickListener {
            monthCurent.add(Calendar.MONTH, 1)
            callBacks()
        }

        bind.back.setOnClickListener {
            monthCurent.add(Calendar.MONTH, -1)
            callBacks()
        }
    }

    private fun initViews(attrs: AttributeSet?, defStyle: Int, defStyleRes: Int) {
        attrs ?: return
        val attributeSet = context.obtainStyledAttributes(
            attrs,
            R.styleable.CalendarFillView,
            defStyle,
            defStyleRes
        )
        with(attributeSet) {
            try {
                colors.primary =
                    getResourceId(R.styleable.CalendarFillView_fill_colorPrimary, colors.primary)
                colors.secondary = getResourceId(
                    R.styleable.CalendarFillView_fill_colorSecondary,
                    colors.secondary
                )
                colors.default =
                    getResourceId(R.styleable.CalendarFillView_fill_default, colors.default)
                colors.selected =
                    getResourceId(R.styleable.CalendarFillView_fill_selected, colors.selected)
                colors.other = getResourceId(R.styleable.CalendarFillView_fill_other, colors.other)
                typeCallBack = getInt(R.styleable.CalendarFillView_type_callback, 0).let {
                    when (it) {
                        CallBack.WITH_RETURN.ordinal -> CallBack.WITH_RETURN
                        CallBack.NOT_RETURN.ordinal -> CallBack.NOT_RETURN
                        else -> CallBack.BOTH
                    }
                }
            } finally {
                recycle()
            }
        }
    }

    private fun fillWeekDayLabel() {
        val aux: Calendar = Calendar.getInstance()
        var account = 0
        do {
            val week = SimpleDateFormat(FORMAT_WEEK, Locale.getDefault()).format(aux.time)
            when (aux[Calendar.DAY_OF_WEEK]) {
                Calendar.SUNDAY -> {
                    bind.labelSunday.text = week
                }
                Calendar.MONDAY -> {
                    bind.labelMonday.text = week
                }
                Calendar.TUESDAY -> {
                    bind.labelTuesday.text = week
                }
                Calendar.WEDNESDAY -> {
                    bind.labelWednesday.text = week
                }
                Calendar.THURSDAY -> {
                    bind.labelThursday.text = week
                }
                Calendar.FRIDAY -> {
                    bind.labelFriday.text = week
                }
                Calendar.SATURDAY -> {
                    bind.labelSaturday.text = week
                }
            }
            ++account
            aux.set(Calendar.DATE, account)
        } while (account <= 7)
    }


    fun initSetupToolbarCalendar(
        initMonth: Calendar,
        datesColors: Array<CalendarioItem> = arrayOf(),
        colors: ColorFill? = null,
        notReturn: (Calendar) -> Unit = {},
        withReturn: (Calendar) -> Array<CalendarioItem> = { emptyArray() },
        adapter: CalendarFillBaseAdapter? = defaultAdapter
    ) {
        monthCurent = initMonth
        dateSelected = initMonth.time
        colors?.let { this.colors = it }
        callDate = notReturn
        callUpdates = withReturn
        adapter?.let { defaultAdapter = adapter.apply {
//            dates = fillDaysOnVisible()
//            notifyDataSetChanged()
        } }
        updateDates(datesColors = datesColors)
        callBacks()
    }

    private fun callBacks() {
        when (typeCallBack) {
            CallBack.NOT_RETURN -> callDate(monthCurent)
            CallBack.WITH_RETURN -> {
                val datesColors = callUpdates(monthCurent)
                updateDates(datesColors = datesColors, selectedMoth = monthCurent)
            }
            CallBack.BOTH -> {
                val datesColors = callUpdates(monthCurent)
                updateDates(datesColors = datesColors, selectedMoth = monthCurent)
                callDate(monthCurent)
            }
        }
    }

    private fun updateDescriptionMonth(calendar: Calendar) {
        bind.monthYear.text = SimpleDateFormat(FORMAT_MONTH_YEAR, Locale.getDefault())
            .format(calendar.time)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    fun updateDates(
        datesColors: Array<CalendarioItem> = arrayOf(),
        selectedMoth: Calendar = monthCurent
    ) {
        updateDescriptionMonth(selectedMoth.clone() as Calendar)
        bind.gridCalendar.adapter = defaultAdapter.apply {
            selectedDay = dateSelected
            dateColors = datesColors
            currentMonth = selectedMoth
            dates = fillDaysOnVisible(selectedMoth)
            notifyDataSetChanged()
        }

        bind.gridCalendar.setOnItemClickListener { parent, _, position, _ ->
            val calendarItemSelected = getCalendarItem(informations = datesColors, dateSelected)
            dateSelected = parent.getItemAtPosition(position) as Date
            onclick(dateSelected, calendarItemSelected)
            bind.gridCalendar.adapter = defaultAdapter.apply {
                selectedDay = dateSelected
                dateColors = datesColors
                notifyDataSetChanged()
            }
        }
    }

    fun fillDaysOnVisible(selectedMonth: Calendar = monthCurent): MutableList<Date> {
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

    override fun getContextDiario(): Context = context
    override fun getCalendarItem(informations: Array<CalendarioItem>, date: Date): CalendarioItem? {
        return informations.firstOrNull { info -> date.isSame(info.dayOn()) }
    }
}