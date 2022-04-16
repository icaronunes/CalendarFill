package br.com.mirespeiti.calendarfill.calendar.ext

import android.content.Context
import android.widget.Toast

fun Context.makeToast(txt: Int) {
    makeToast(getString(txt))
}

fun Context.makeToast(txt: String) {
    Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
}