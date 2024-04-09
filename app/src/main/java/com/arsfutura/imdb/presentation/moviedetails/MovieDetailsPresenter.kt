package com.arsfutura.imdb.presentation.moviedetails

import com.arsfutura.imdb.domain.models.MovieDetailsModel
import com.arsfutura.imdb.domain.repositories.FavoritesRepository
import com.arsfutura.imdb.domain.repositories.MovieDetailsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailsPresenter(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val view: MovieDetailsMvp.View
) : MovieDetailsMvp.Presenter {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private var movieDetailsModel: MovieDetailsModel? = null

    override fun initialize() {
    }

    override fun subscribe() {
        disposable.add(
            movieDetailsRepository.subscribeToMovieDetails()
                .skip(1)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { movieDetailsModel = it }
                .subscribe { view.loadMovie(it) }
        )
        disposable.add(
            movieDetailsRepository.loadingObserver()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.setLoading(it) }
        )
        disposable.add(
            movieDetailsRepository.errorObserver()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.showError(it) }
        )
    }

    override fun destroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun getDetailsFor(id: String) {
        movieDetailsRepository.getMovieWithId(id)
    }

    override fun toggleFavoriteMovie() {
        movieDetailsModel?.let { movie ->
            disposable.add(
                favoritesRepository
                    .toggleFavorite(movie.movieId)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer { view.setFavorite(it) })
            )
        }
    }
}
