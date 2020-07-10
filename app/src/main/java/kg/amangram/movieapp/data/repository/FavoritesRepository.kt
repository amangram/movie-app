package kg.amangram.movieapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import kg.amangram.movieapp.data.local.FavoritesDAO
import kg.amangram.movieapp.data.model.MovieById

class FavoritesRepository(private val dao: FavoritesDAO) {

    fun getFavorites(): LiveData<List<MovieById>>{
        val movies = MutableLiveData<List<MovieById>>()
        val disposable = dao.getFavorites()
            .subscribeOn(Schedulers.io())
            .subscribe({
                movies.postValue(it)
            },{})
        return movies
    }

    fun deleteMovie(movie: MovieById){
        val disposable = dao.delete(movie)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun addMovie(movie: MovieById){
        val disposable = dao.add(movie)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}