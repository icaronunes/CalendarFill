package repository

import br.com.mirespeiti.data.ResponseReview
import java.util.*

interface ReviewsRepository {
    suspend fun getReviewsNYTime(month: Calendar?): ResponseReview
}
