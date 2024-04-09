package com.arsfutura.imdb.data.repo

import android.util.Log
import com.arsfutura.imdb.data.api.MoviesApi
import com.arsfutura.imdb.data.entity.Favorite
import com.arsfutura.imdb.data.local.FavoriteDao
import com.arsfutura.imdb.data.mapper.MovieMapper
import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.domain.repositories.FavoritesRepository
import com.arsfutura.imdb.domain.repositories.MovieListRepository
import com.arsfutura.imdb.presentation.utils.SortMoviesByYear
import com.arsfutura.imdb.presentation.utils.createAdapterData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class FavoritesRepositoryImpl(
    private val api: MoviesApi,
    private val favoriteDao: FavoriteDao,
    private val moduleListRepository: MovieListRepository,
) :
    FavoritesRepository {
    private val subject: BehaviorSubject<List<MovieModel>> =
        BehaviorSubject.createDefault(arrayListOf())
    private val error: PublishSubject<Throwable> = PublishSubject.create()
    private val loading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun subscribeToFavorites(): Observable<List<MovieModel>> {
        return subject.hide()
    }

    override fun errorObserver(): Observable<Throwable> {
        return error.hide()
    }

    override fun loadingObserver(): Observable<Boolean> {
        return loading.hide()
    }

    override fun getFavorites() {
        Single.create<List<Favorite>> {
            val favorites = favoriteDao.getAll()
            Log.d("db dump", favorites.toString())
            it.onSuccess(favorites)
        }
            .subscribeOn(Schedulers.io())
            .toObservable()
            .flatMap { favorites ->
                Observable.fromIterable(favorites)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        api.getMovie(it.id)
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                    }.map {
                        MovieMapper().toMovieModel(it)
                    }.toList()
                    .doOnSubscribe { loading.onNext(true) }
                    .doFinally { loading.onNext(false) }
                    .map {
                        Collections.sort(it, SortMoviesByYear())
                        it
                    }
                    .map {
                        createAdapterData(it)
                    }
                    .map {
                        subject.onNext(it)
                    }
                    .toObservable()
            }.subscribe()
    }

    override fun toggleFavorite(id: String, refresh: Boolean): Single<Boolean> {
        return Single.create {
            try {
                val favorite = favoriteDao.isFavorite(id)
                val result = Favorite(id)
                val isFavorite = if (favorite == null) {
                    favoriteDao.favorite(result)
                    true
                } else {
                    favoriteDao.unFavorite(result)
                    false
                }
                if (refresh) getFavorites()
                moduleListRepository.refreshLocalList()
                it.onSuccess(isFavorite)
            } catch (e: Exception) {
                Log.e(
                    MovieDetailsRepositoryImpl::class.simpleName,
                    "Cannot favorite this movie because: ${e.message}"
                )
                it.onSuccess(false)
            }
        }
    }
}