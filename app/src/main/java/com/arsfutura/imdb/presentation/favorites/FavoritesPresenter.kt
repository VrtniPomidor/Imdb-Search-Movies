package com.arsfutura.imdb.presentation.favorites

import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.domain.repositories.FavoritesRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoritesPresenter(
    private val favoritesRepository: FavoritesRepository,
    private val view: FavoritesMvp.View,
) : FavoritesMvp.Presenter {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun initialize() {
        getFavoriteMovies()
    }

    override fun subscribe() {
        disposable.add(
            favoritesRepository.subscribeToFavorites()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.loadFavorites(it) },
        )
        disposable.add(
            favoritesRepository.loadingObserver()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.setLoading(it) }
        )
        disposable.add(
            favoritesRepository.errorObserver()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.showError(it) }
        )
    }

    override fun toggleFavoriteMovie(movie: MovieModel) {
        favoritesRepository
            .toggleFavorite(movie.movieId, false)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun destroy() {
        if (!disposable.isDisposed) disposable.dispose()
    }

    private fun getFavoriteMovies() {
        favoritesRepository.getFavorites()
    }

    override fun refresh() {
        getFavoriteMovies()
    }
}
