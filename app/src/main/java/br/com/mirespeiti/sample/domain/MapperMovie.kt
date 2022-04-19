package br.com.mirespeiti.sample.domain

import br.com.mirespeiti.data.Result
import br.com.mirespeiti.data.convertFromDate
import javax.inject.Inject

class MapperMovie @Inject constructor() : MapperToDTO<Result, ReviewFill> {
    override fun to(to: Result): ReviewFill {
        return with(to) {
            ReviewFill(
                event = displayTitle,
                work = true,
                dayOn = convertFromDate()
            )
        }
    }
}