package br.com.mirespeiti.calendarfill.domain

interface MapperToDTO<T, E> {
    fun to(to: T): E
}