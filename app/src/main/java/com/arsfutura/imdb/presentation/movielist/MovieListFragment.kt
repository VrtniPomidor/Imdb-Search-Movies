package com.arsfutura.imdb.presentation.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsfutura.imdb.databinding.FragmentMoviesBinding
import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.presentation.common.BaseFragment
import com.arsfutura.imdb.presentation.common.BaseMvp
import com.arsfutura.imdb.presentation.common.adapter.MovieRecyclerAdapter
import com.arsfutura.imdb.presentation.moviedetails.MovieDetailsActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class MovieListFragment : BaseFragment(), MovieListMvp.View {
    companion object {
        fun newInstance(): MovieListFragment {
            return MovieListFragment()
        }
    }

    private lateinit var binding: FragmentMoviesBinding
    private val presenter: MovieListPresenter by inject { parametersOf(this) }

    private lateinit var adapter: MovieRecyclerAdapter
    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MovieRecyclerAdapter(arrayListOf())
        binding.recyclerView.adapter = adapter
        setupAdapterItemClick()
        setUpLoadMoreListener()

        binding.etMovieSearch.doOnTextChanged { text, _, _, _ ->
            presenter.searchMovies(text.toString())
        }
        binding.etMovieSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus()
            }
            false
        }

        binding.loading.setOnRefreshListener { presenter.searchMovies(binding.etMovieSearch.text.toString()) }
        binding.etMovieSearch.requestFocus()
    }

    override fun loadMovies(movies: List<MovieModel>) {
        adapter.reset(movies)
        handleEmptyView(movies.isEmpty())
        blocker = false
    }

    private fun setupAdapterItemClick() {
        disposable.add(adapter.clickEvent
            .subscribe {
                navigateToMovieDetails(it.movieId)
            }
        )
        disposable.add(adapter.favoriteClick
            .subscribe {
                presenter.toggleFavoriteMovie(it)
            }
        )
    }

    private fun navigateToMovieDetails(movieId: String) {
        startActivity(MovieDetailsActivity.createIntent(requireContext(), movieId))
    }

    private fun handleEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerView.visibility = GONE
            binding.empty.visibility = VISIBLE
        } else {
            binding.recyclerView.visibility = VISIBLE
            binding.empty.visibility = GONE
        }
    }

    override fun setLoading(isLoading: Boolean) {
        binding.loading.isRefreshing = isLoading
    }

    override fun getPresenter(): BaseMvp.Presenter {
        return presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) disposable.dispose()
    }

    private var blocker: Boolean = false

    private fun setUpLoadMoreListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager!!.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible + 1 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached && !blocker) {
                    blocker = true
                    presenter.nextPage(binding.etMovieSearch.text.toString())
                }
            }
        })
    }
}
