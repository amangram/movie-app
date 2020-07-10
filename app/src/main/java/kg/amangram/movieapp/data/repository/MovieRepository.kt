package kg.amangram.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import kg.amangram.movieapp.data.api.MovieApi
import kg.amangram.movieapp.data.model.MovieById
import kg.amangram.movieapp.data.model.State

class MovieRepository(private val api: MovieApi) {

    fun getMovies(page: Int) = api.getMovies(page)

    fun search(page: Int, text: String) = api.searchMovie(page, text)

    fun getMovieById(id: Int): LiveData<State<MovieById>> {
        val movie = MutableLiveData<State<MovieById>>()
        movie.postValue(State.loading())
        val disposable = api.getMovie(id)
            .subscribeOn(Schedulers.io())
            .subscribe({response->
                if (response.isSuccessful){
                    response.body()?.let {_movie->
                        movie.postValue(State.success(_movie))
                    }
                }
            }, {
                it.printStackTrace()
                movie.postValue(State.failed(it.localizedMessage))
            })
        return movie
    }
}