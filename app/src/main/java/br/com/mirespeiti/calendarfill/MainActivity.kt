package br.com.mirespeiti.calendarfill

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
                    bind.fillCalendar.updateDates(it.reviewsList)
                }
            }
        }

        bind.fillCalendar.onclick = { date, item ->
            makeToast(item?.event().toString())
        }

        bind.fillCalendar.initSetupToolbarCalendar(
            initMonth = Calendar.getInstance(),
            datesColors = viewModel.fakeDates(Calendar.getInstance()),
            notReturn = {
                viewModel.getReviewsOnMonth(it)
            },
            withReturn = {
                viewModel.fakeDates(it)
            })
    }
}