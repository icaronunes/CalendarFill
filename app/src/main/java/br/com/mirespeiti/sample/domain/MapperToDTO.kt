package br.com.mirespeiti.sample.domain

interface MapperToDTO<T, E> {
    fun to(to: T): E
}