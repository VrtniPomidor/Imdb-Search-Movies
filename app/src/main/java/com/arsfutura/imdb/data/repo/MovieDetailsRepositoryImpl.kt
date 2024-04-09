package com.arsfutura.imdb.data.repo

import com.arsfutura.imdb.data.api.MoviesApi
import com.arsfutura.imdb.data.local.FavoriteDao
import com.arsfutura.imdb.data.mapper.MovieMapper
import com.arsfutura.imdb.domain.models.MovieDetailsModel
import com.arsfutura.imdb.domain.repositories.MovieDetailsRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class MovieDetailsRepositoryImpl(
    private val api: MoviesApi,
    private val favoriteDao: FavoriteDao
) :
    MovieDetailsRepository {
    private val subject: BehaviorSubject<MovieDetailsModel> =
        BehaviorSubject.createDefault(MovieDetailsModel())
    private val error: PublishSubject<Throwable> = PublishSubject.create()
    private val loading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun subscribeToMovieDetails(): Observable<MovieDetailsModel> {
        return subject.hide()
    }

    override fun errorObserver(): Observable<Throwable> {
        return error.hide()
    }

    override fun loadingObserver(): Observable<Boolean> {
        return loading.hide()
    }

    override fun getMovieWithId(id: String) {
        api.getMovie(id)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { loading.onNext(true) }
            .doFinally { loading.onNext(false) }
            .map {
                val isFavorite = favoriteDao.isFavorite(it.imdbId) != null
                MovieMapper().toMovieDetailsModel(it, isFavorite)
            }
            .doOnSuccess {
                subject.onNext(it)
            }.doOnError { error.onNext(it) }
            .subscribe()
    }
}
