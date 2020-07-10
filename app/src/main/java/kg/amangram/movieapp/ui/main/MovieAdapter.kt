package kg.amangram.movieapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.amangram.movieapp.R
import kg.amangram.movieapp.data.api.Constants.Companion.POSTER_BASE_URL
import kg.amangram.movieapp.data.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(private val interaction: (Int) -> Unit) :
    PagedListAdapter<Movie, MovieAdapter.MovieVH>(MovieDC()) {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return MovieVH(inflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) {
            itemView.item_movie_title.text = item.title
            itemView.item_movie_release_date.text = item.releaseDate
            Glide.with(context)
                .load(POSTER_BASE_URL+item.posterPath)
                .centerCrop()
                .into(itemView.item_movie_poster)
            itemView.setOnClickListener { interaction(item.id) }
        }
    }

    private class MovieDC : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem == newItem
        }
    }
}