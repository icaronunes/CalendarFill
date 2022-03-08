package br.com.mirespeiti.calendarfill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import br.com.mirespeiti.calendarfill.calendardiario.Calendario
import br.com.mirespeiti.calendarfill.calendardiario.CalendarioItem
import br.com.mirespeiti.calendarfill.calendardiario.adapter.makeToast
import br.com.mirespeiti.calendarfill.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var bind: ActivityMainBinding

    @Inject
    lateinit var name: AnalyticsServiceImpl2

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.fillCalendar.setupToolbarCalendar(
            activity = this,
            initMonth = Calendar.getInstance()
        )

        bind.fillCalendar.callDataColors = {
            val colors = (1..25).map { number ->
                val c = Calendar.getInstance()
                c.time = it.time
                c.add(Calendar.DATE, number)
                val date = c.time
                val item = Calendario(
                    date = date,
                    work = number % 5 == 0,
                    event = if (number == 24) "nada" else "null"
                )
                item
            }.toTypedArray<CalendarioItem>()
            colors
        }

        bind.fillCalendar.onclick = { date, _ ->
            makeToast(date.toGMTString())
        }
    }
}