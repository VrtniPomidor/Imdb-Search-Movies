package com.arsfutura.imdb.presentation.favorites

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsfutura.imdb.R
import com.arsfutura.imdb.databinding.FragmentFavoritesBinding
import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.presentation.common.BaseFragment
import com.arsfutura.imdb.presentation.common.BaseMvp
import com.arsfutura.imdb.presentation.common.adapter.MovieRecyclerAdapter
import com.arsfutura.imdb.presentation.moviedetails.MovieDetailsActivity
import com.arsfutura.imdb.presentation.movielist.MovieListActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class FavoritesFragment : BaseFragment(), FavoritesMvp.View {
    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    private lateinit var binding: FragmentFavoritesBinding
    private val presenter: FavoritesPresenter by inject { parametersOf(this) }

    private lateinit var adapter: MovieRecyclerAdapter
    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_favorites, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.search -> {
                        startActivity(MovieListActivity.createIntent(requireContext()))
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.loading.setOnRefreshListener { presenter.refresh() }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MovieRecyclerAdapter(arrayListOf())
        binding.recyclerView.adapter = adapter
        setupAdapterItemClick()
    }

    override fun loadFavorites(favorites: List<MovieModel>) {
        adapter.reset(favorites)
        handleEmptyView(favorites.isEmpty())
    }

    private fun handleEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerView.visibility = View.GONE
            binding.empty.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.empty.visibility = View.GONE
        }
    }

    override fun setLoading(isLoading: Boolean) {
        binding.loading.isRefreshing = isLoading
    }

    override fun getPresenter(): BaseMvp.Presenter {
        return presenter
    }

    private fun setupAdapterItemClick() {
        disposable.add(adapter.clickEvent
            .subscribe {
                navigateToMovieDetails(it.movieId)
            }
        )
        disposable.add(adapter.favoriteClick
            .subscribe {
                adapter.remove(it)
                presenter.toggleFavoriteMovie(it)
            }
        )
        disposable.add(adapter.sizeChange
            .subscribe {
                handleEmptyView(it == 0)
            }
        )
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) disposable.dispose()
        super.onDestroy()
    }

    private fun navigateToMovieDetails(movieId: String) {
        startActivity(MovieDetailsActivity.createIntent(requireContext(), movieId))
    }
}