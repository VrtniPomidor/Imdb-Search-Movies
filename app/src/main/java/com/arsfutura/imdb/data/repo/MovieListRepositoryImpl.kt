package com.arsfutura.imdb.data.repo

import com.arsfutura.imdb.data.api.MoviesApi
import com.arsfutura.imdb.data.errors.ApiError
import com.arsfutura.imdb.data.local.FavoriteDao
import com.arsfutura.imdb.data.mapper.MovieMapper
import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.domain.models.PagingModel
import com.arsfutura.imdb.domain.repositories.MovieListRepository
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*


class MovieListRepositoryImpl(
    private val api: MoviesApi,
    private val favoriteDao: FavoriteDao,
) : MovieListRepository {
    private val subject = BehaviorSubject.createDefault<List<MovieModel>>(arrayListOf())
    private val paging = PublishProcessor.create<PagingModel>()
    private val error = PublishSubject.create<Throwable>()
    private val loading = BehaviorSubject.createDefault(false)

    private var isPagingDone: Boolean = false
    private var page: Int = 1

    init {
        subscribeForData()
    }

    override fun subscribeToMovieList(): Observable<List<MovieModel>> {
        return subject.hide()
    }

    override fun errorObserver(): Observable<Throwable> {
        return error.hide()
    }

    override fun loadingObserver(): Observable<Boolean> {
        return loading.hide()
    }

    override fun nextPage(search: String) {
        if (search.isNotBlank()) paging.onNext(PagingModel(search, page++))
    }

    override fun getMovies(search: String) {
        isPagingDone = false
        page = 1
        paging.onNext(PagingModel(search, page))
    }

    override fun refreshLocalList() {
        refreshLocally()
    }

    /**
     * No sort here and no headers
     */
    private fun getMovies(search: String, page: Int): @NonNull Single<List<MovieModel>> {
        return api.getMoviesByTitle(search, page)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { loading.onNext(true) }
            .doFinally { loading.onNext(false) }
            .doOnError { error.onNext(it) }
            .map {
                if (it.error.isNotBlank()) {
                    throw ApiError(it.error)
                }
                it
            }
            .map {
                if (it.totalResults.toInt() <= (page * 10)) {
                    isPagingDone = true
                }
                MovieMapper().toMovieModel(it, favoriteDao)
            }.map {
                val temp = mutableListOf<MovieModel>()
                if (page != 1) {
                    // append
                    temp.addAll(subject.value ?: arrayListOf())
                }
                temp.addAll(it ?: arrayListOf())
                temp.toList()
            }.doOnSuccess {
                subject.onNext(it.toList())
            }
    }

    private fun subscribeForData() {
        paging
            .onBackpressureDrop()
            .filter { !isPagingDone }
            .filter { loading.value == false }
            .concatMapSingle { page ->
                getMovies(page.search, page.page)
                    .subscribeOn(Schedulers.io())
                    .doOnError { error.onNext(it) }
                    .onErrorReturn { ArrayList() }
            }.subscribe()
    }

    private fun refreshLocally() {
        val value = subject.value
        value?.let {
            Observable.fromIterable(value)
                .subscribeOn(Schedulers.io())
                .map { movieModel ->
                    movieModel.isFavorite = favoriteDao.isFavorite(movieModel.movieId) != null
                    movieModel
                }.toList()
                .map { list ->
                    subject.onNext(list)
                }
                .subscribe()
        }
    }
}