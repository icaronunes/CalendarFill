package br.com.mirespeiti.calendarfill.calendardiario

import android.view.View
import androidx.core.view.isVisible

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}