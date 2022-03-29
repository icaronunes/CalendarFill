package repository

import api.NyTime
import br.com.mirespeiti.data.ResponseReview
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReviewsRepositoryImp @Inject constructor(
    private val apiService: NyTime
) : ReviewsRepository {

    override suspend fun getReviewsNYTime(month: Calendar?): ResponseReview {
        val paramsOnMonth = month?.initAndFinal()
        return apiService.getNYMovies(paramsOnMonth)
    }
}

private fun Calendar.initAndFinal(): String {
    set(Calendar.DAY_OF_MONTH, 1)
    val init = SimpleDateFormat("yyyy-MM-dd").format(time)
    return "${init}:${dayFinal()}"
}

private fun Calendar.dayFinal(): String {
    val lastDay = getActualMaximum(Calendar.DAY_OF_MONTH)
    set(Calendar.DAY_OF_MONTH, lastDay)
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(time)
}
