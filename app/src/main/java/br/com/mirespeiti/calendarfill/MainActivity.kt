package br.com.mirespeiti.calendarfill

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.mirespeiti.calendarfill.calendar.CalendarFillView
import br.com.mirespeiti.calendarfill.calendar.adapter.FunnyAdapterFill
import br.com.mirespeiti.calendarfill.calendar.ext.makeToast
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
                viewModel.state.collect { itens ->
                    bind.fillCalendar.updateDates(itens.reviewsList)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.stateFake.collect { fake ->
                    bind.fillFake.updateDates(fake.reviewsList)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.stateFake.collect { fake ->
                    bind.fillCustom.updateDates(fake.reviewsList)
                }
            }
        }

        bind.fillCalendar.initSetupToolbarCalendar(
            initMonth = Calendar.getInstance(),
            datesColors = viewModel.fakeDates(Calendar.getInstance()),
            notReturn = {
                viewModel.getReviewsOnMonth(it)
            }
        )

        bind.fillFake.initSetupToolbarCalendar(
            initMonth = Calendar.getInstance(),
            datesColors = viewModel.fakeDates(Calendar.getInstance()),
            notReturn = {
                viewModel.getFakeDates(it)
            },
            adapter = FunnyAdapterFill(
                calendarFillContext = bind.fillFake,
                selectedDay = Date()
            )
        )

        bind.fillCustom.initSetupToolbarCalendar(
            initMonth = Calendar.getInstance(),
            datesColors = viewModel.fakeDates(Calendar.getInstance()),
            notReturn = { viewModel.getFakeDates(it) }
        )

        bind.fillCustom.onclick = { date, item ->
            makeToast(item?.event().toString())
        }

        bind.fillFake.onclick = { date, item ->
            makeToast(item?.event().toString())
        }

        bind.fillCalendar.onclick = { date, item ->
            makeToast(item?.event().toString())
        }

    }
}