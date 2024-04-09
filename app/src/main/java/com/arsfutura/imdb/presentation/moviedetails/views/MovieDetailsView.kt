package com.arsfutura.imdb.presentation.moviedetails.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsfutura.imdb.databinding.ViewMovieDetailsBinding
import com.arsfutura.imdb.domain.models.MovieDetailsModel
import com.arsfutura.imdb.presentation.utils.loadPicasso
import java.util.*

class MovieDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewMovieDetailsBinding =
        ViewMovieDetailsBinding.inflate(LayoutInflater.from(context), this, true)

    fun setupView(movie: MovieDetailsModel) {
        loadPicasso(movie.imageUrl, binding.movieImage, binding.progressImage)
        binding.movieTitle.text = movie.title
        binding.movieYear.text =
            String.format(Locale.getDefault(), "%s - %s", movie.released, movie.runtime)
        binding.movieScore.text = movie.rating
        binding.movieGenre.text = movie.genre
        binding.movieDescription.text = movie.description
    }
}