package api

import br.com.mirespeiti.data.ResponseReview
import retrofit2.http.GET
import retrofit2.http.Query

interface NyTime {

    @GET("movies/v2/reviews/search.json?")
    suspend fun getNYMovies(@Query("opening-date") month: String?): ResponseReview
}
