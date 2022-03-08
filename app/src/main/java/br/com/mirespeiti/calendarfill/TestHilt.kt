package br.com.mirespeiti.calendarfill

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject


interface AnalyticsService {
    var name: String
}

class AnalyticsServiceImpl @Inject constructor(
) : AnalyticsService {
    override var name: String = "pessoa"
}


class AnalyticsServiceImpl2 @Inject constructor(
) : AnalyticsService {
    override var name: String = "pessoa2"
}

@Module
@InstallIn(ActivityComponent::class)
object AnalyticsModule {

    @Provides
    fun nome(
        // Potential dependencies of this type
    ) = "AnalyticsServiceImpl()"


    @Provides
    fun nome2(
        // Potential dependencies of this type
    )= "AnalyticsServiceImpl2()"

}
