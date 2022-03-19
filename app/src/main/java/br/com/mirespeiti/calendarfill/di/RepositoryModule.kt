package br.com.mirespeiti.calendarfill.di

import br.com.mirespeiti.calendarfill.domain.MapperMovie
import br.com.mirespeiti.calendarfill.domain.MapperToDTO
import br.com.mirespeiti.calendarfill.domain.ReviewFill
import br.com.mirespeiti.data.Result
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import repository.ReviewsRepositoryImp
import repository.ReviewsRepository

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideReviewsRespositoryImp(repositoryImp: ReviewsRepositoryImp): ReviewsRepository

    @Binds
    abstract fun bindsMapperReviews(mapper: MapperMovie): MapperToDTO<Result, ReviewFill>
}