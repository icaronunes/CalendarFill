package br.com.mirespeiti.calendarfill

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.mirespeiti.calendarfill.calendardiario.domain.Calendario
import br.com.mirespeiti.calendarfill.calendardiario.domain.CalendarioItem
import br.com.mirespeiti.calendarfill.calendardiario.ext.makeToast
import br.com.mirespeiti.calendarfill.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var bind: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    Log.d(this.javaClass.name, it.toString())
                    bind.fillCalendar.updateDates(it.reviewsList)
                }
            }
        }

        bind.fillCalendar.onclick = { date, item ->
            makeToast(item?.event().toString())
        }

        bind.fillCalendar.initSetupToolbarCalendar(
            initMonth = Calendar.getInstance(),
            datesColors = fakeDates(Calendar.getInstance()),
            notReturn = {
                makeToast("E nada")
                viewModel.getReviewsOnMonth(it)
            },
            withReturn = {
                fakeDates(it)
            })
    }

    private fun fakeDates(it: Calendar): Array<CalendarioItem> {
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
        return colors
    }
}