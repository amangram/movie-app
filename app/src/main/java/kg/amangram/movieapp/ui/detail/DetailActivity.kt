package kg.amangram.movieapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kg.amangram.movieapp.R
import kg.amangram.movieapp.data.api.Constants.Companion.POSTER_BASE_URL
import kg.amangram.movieapp.data.model.MovieById
import kg.amangram.movieapp.data.model.State
import kg.amangram.movieapp.gone
import kg.amangram.movieapp.toast
import kg.amangram.movieapp.visible
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModel{
        parametersOf(intent.getIntExtra("MOVIE_ID",0))
    }
    private val favoritesList = ArrayList<MovieById>()
    private var bookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        getData()
        settingToolbar()
        bookmarkClickListener()
    }

    private fun bookmarkClickListener() {
        iv_bookmark.setOnClickListener {
            if (bookmarked) {
                viewModel.deleteMovie()
                iv_bookmark.setImageResource(R.drawable.ic_bookmark)
                bookmarked = false
            } else {
                viewModel.addMovie()
                iv_bookmark.setImageResource(R.drawable.ic_unbookmark)
                bookmarked = true
            }
        }
    }

    private fun getData(){
        viewModel.movie.observe(this, Observer { state->
            bindData(state)
        })
        viewModel.favorites.observe(this, Observer { favorites->
            Log.d("TAG", favorites.size.toString())
            if (favorites.isNullOrEmpty())
                favoritesList.addAll(emptyList())
            else
            favoritesList.addAll(favorites)
        })
    }

    private fun bindData(state: State<MovieById>){
        when(state){
            is State.Loading-> pb_detail.visible()
            is State.Success->{
                pb_detail.gone()
                state.data.let { movie->
                    Glide.with(this)
                        .load(POSTER_BASE_URL+movie.posterPath)
                        .centerCrop()
                        .into(iv_movie_poster)
                    tv_movie_rating.text = movie.rating.toString()
                    tv_movie_title.text = movie.title
                    tv_movie_overview.text = movie.overview
                    tv_movie_release_date.text = movie.releaseDate
                    checkFavorites(movie)
                }
            }
            is State.Failed ->{
                pb_detail.gone()
                toast(getString(R.string.connection_error))
            }
        }
    }

    private fun checkFavorites(movie: MovieById){
        if (favoritesList.contains(movie)){
            bookmarked = true
            iv_bookmark.setImageResource(R.drawable.ic_unbookmark)
        }else{
            bookmarked = false
            iv_bookmark.setImageResource(R.drawable.ic_bookmark)
        }
    }

    private fun settingToolbar() {
        supportActionBar?.title = getString(R.string.about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}