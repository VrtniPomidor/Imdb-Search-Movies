package com.arsfutura.imdb.presentation.movielist

import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.domain.repositories.FavoritesRepository
import com.arsfutura.imdb.domain.repositories.MovieListRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class MovieListPresenter(
    private val movieListRepository: MovieListRepository,
    private val favoritesRepository: FavoritesRepository,
    private val view: MovieListMvp.View
) : MovieListMvp.Presenter {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val searchObs: BehaviorSubject<String> =
        BehaviorSubject.createDefault("")


    override fun initialize() {
    }

    override fun subscribe() {
        disposable.add(
            movieListRepository.subscribeToMovieList()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.loadMovies(it) },
        )
        disposable.add(
            movieListRepository.loadingObserver()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.setLoading(it) }
        )
        disposable.add(
            movieListRepository.errorObserver()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.showError(it) }
        )
        disposable.add(
            searchObs
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter {
                    val notBlank = it.isNotBlank()
                    if (!notBlank) view.setLoading(false)
                    notBlank
                }
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe { movieListRepository.getMovies(it) }
        )
    }

    override fun destroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun searchMovies(text: String) {
        searchObs.onNext(text.trim())
    }

    override fun toggleFavoriteMovie(movie: MovieModel) {
        favoritesRepository
            .toggleFavorite(movie.movieId)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun nextPage(search: String) {
        movieListRepository.nextPage(search)
    }
}
