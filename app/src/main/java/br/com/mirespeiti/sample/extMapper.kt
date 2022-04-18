package br.com.mirespeiti.sample

import br.com.mirespeiti.sample.domain.MapperMovie
import br.com.mirespeiti.sample.domain.ReviewFill
import br.com.mirespeiti.data.Result

fun Result.mapTo(mapper: MapperMovie): ReviewFill {
    return mapper.to(this)
}