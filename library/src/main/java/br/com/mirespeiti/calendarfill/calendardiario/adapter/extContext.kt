package br.com.mirespeiti.calendarfill.calendardiario.adapter

import android.content.Context
import android.widget.Toast
import br.com.mirespeiti.calendarfill.R

fun Context.makeToast(txt: Int) {
    makeToast(getString(txt))
}

fun Context.makeToast(txt: String) {
    Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
}