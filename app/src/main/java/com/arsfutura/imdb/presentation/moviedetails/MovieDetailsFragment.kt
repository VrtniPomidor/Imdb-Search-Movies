package com.arsfutura.imdb.presentation.moviedetails

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.arsfutura.imdb.R
import com.arsfutura.imdb.databinding.FragmentMovieDetailsBinding
import com.arsfutura.imdb.domain.models.MovieDetailsModel
import com.arsfutura.imdb.presentation.common.BaseFragment
import com.arsfutura.imdb.presentation.common.BaseMvp
import com.arsfutura.imdb.presentation.common.Constants
import com.arsfutura.imdb.presentation.utils.getFavoriteIcon
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class MovieDetailsFragment : BaseFragment(), MovieDetailsMvp.View, AndroidScopeComponent {
    companion object {
        fun newInstance(id: String): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = Bundle().apply { putString(Constants.idKey, id) }
            }
        }
    }

    override val scope: Scope by fragmentScope()

    private lateinit var binding: FragmentMovieDetailsBinding
    private val presenter: MovieDetailsPresenter by inject { parametersOf(this) }

    private var menu: Menu? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun getPresenter(): BaseMvp.Presenter {
        return presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                this@MovieDetailsFragment.menu = menu
                menuInflater.inflate(R.menu.menu_movie_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.favorite -> {
                        onFavorite()
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)

        binding.swipeRefresh.setOnRefreshListener { getDetails() }
        binding.viewMovieDetails.visibility = GONE
        getDetails()
    }

    private fun getDetails() {
        presenter.getDetailsFor(arguments!!.getString(Constants.idKey)!!)
    }

    private fun onFavorite() {
        presenter.toggleFavoriteMovie()
    }

    override fun loadMovie(movie: MovieDetailsModel) {
        binding.viewMovieDetails.visibility = VISIBLE
        binding.viewMovieDetails.setupView(movie)
        setFavorite(movie.isFavorite)
    }

    override fun setLoading(isLoading: Boolean) {
        binding.swipeRefresh.isRefreshing = isLoading
    }

    override fun setFavorite(isFavorite: Boolean) {
        val iconRes = getFavoriteIcon(isFavorite)
        menu?.findItem(R.id.favorite)?.setIcon(iconRes)
    }
}