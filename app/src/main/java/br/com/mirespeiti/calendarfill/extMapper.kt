package br.com.mirespeiti.calendarfill

import br.com.mirespeiti.calendarfill.domain.MapperMovie
import br.com.mirespeiti.calendarfill.domain.ReviewFill
import br.com.mirespeiti.data.Result

fun Result.mapTo(mapper: MapperMovie): ReviewFill {
    return mapper.to(this)
}