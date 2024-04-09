package com.arsfutura.imdb.presentation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsfutura.imdb.databinding.ItemMovieBinding
import com.arsfutura.imdb.databinding.ItemYearBinding
import com.arsfutura.imdb.domain.models.MovieHeaderModel
import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.presentation.utils.getFavoriteIcon
import com.arsfutura.imdb.presentation.utils.loadPicasso
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class MovieRecyclerAdapter(private val movies: ArrayList<MovieModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val typeHeader = 0
    private val typeItem = 1

    private val clickSubject = PublishSubject.create<MovieModel>()
    val clickEvent: Observable<MovieModel> = clickSubject

    private val favoriteSubject = PublishSubject.create<MovieModel>()
    val favoriteClick: Observable<MovieModel> = favoriteSubject

    private val sizeSubject = PublishSubject.create<Int>()
    val sizeChange: Observable<Int> = sizeSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == typeItem) {
            val itemBinding =
                ItemMovieBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return MovieHolder(itemBinding)
        } else if (viewType == typeHeader) {
            val itemBinding =
                ItemYearBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return MovieHeaderHolder(itemBinding)
        }

        throw RuntimeException("View holder type is wrong!")
    }

    override fun getItemViewType(position: Int): Int {
        if (movies[position] is MovieHeaderModel) {
            return typeHeader
        }
        return typeItem
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemMovie = movies[position]
        if (itemMovie is MovieHeaderModel) {
            (holder as MovieHeaderHolder).bindMovie(itemMovie)
        } else {
            (holder as MovieHolder).bindMovie(itemMovie)
        }
    }

    inner class MovieHolder(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private var movie: MovieModel? = null

        init {
            itemView.setOnClickListener {
                clickSubject.onNext(movie)
            }
        }

        fun bindMovie(movie: MovieModel) {
            this.movie = movie
            loadPicasso(movie.imageUrl, itemBinding.itemImage, itemBinding.progressImage)
            itemBinding.itemTitle.text = movie.title
            itemBinding.ivFavorite.setImageResource(getFavoriteIcon(movie.isFavorite))
            itemBinding.ivFavorite.setOnClickListener {
                movie.isFavorite = !movie.isFavorite
                itemBinding.ivFavorite.setImageResource(getFavoriteIcon(movie.isFavorite))
                favoriteSubject.onNext(movie)
            }
        }
    }

    inner class MovieHeaderHolder(private val itemBinding: ItemYearBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private var movie: MovieHeaderModel? = null

        fun bindMovie(movie: MovieHeaderModel) {
            this.movie = movie
            itemBinding.itemYear.text = movie.headerTitle
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reset(movies: List<MovieModel>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun remove(it: MovieModel) {
        val index = movies.indexOf(it)
        if (index > -1) {
            movies.removeAt(index)
            notifyItemRemoved(index)
            checkHeaders(index)
        }
    }

    private fun checkHeaders(index: Int) {
        if (index in 1 until movies.size) {
            val next = movies[index]
            val prev = movies[index - 1]
            if (next is MovieHeaderModel && prev is MovieHeaderModel) {
                movies.remove(prev)
                notifyItemRemoved(index - 1)
            }
        }
        // Removes header when its the only item
        else if (movies.size == 1) {
            movies.removeAt(0)
            notifyItemRemoved(0)
        }
        // Removes header of last item
        else if (movies.size == index && movies[index - 1] is MovieHeaderModel) {
            movies.removeAt(index - 1)
            notifyItemRemoved(index - 1)
        }
        sizeSubject.onNext(movies.size)
    }
}
