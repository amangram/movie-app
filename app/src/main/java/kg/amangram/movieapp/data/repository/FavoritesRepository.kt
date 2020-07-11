package kg.amangram.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.amangram.movieapp.data.local.FavoritesDAO
import kg.amangram.movieapp.data.model.MovieById

class FavoritesRepository(private val dao: FavoritesDAO) {

    private val compositeDisposable = CompositeDisposable()
    fun getFavorites(): LiveData<List<MovieById>>{
        val movies = MutableLiveData<List<MovieById>>()
        compositeDisposable.add(dao.getFavorites()
            .subscribeOn(Schedulers.io())
            .subscribe({
                movies.postValue(it)
            },{}))
        return movies
    }

    fun deleteMovie(movie: MovieById){
        compositeDisposable.add(dao.delete(movie)
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    fun addMovie(movie: MovieById){
        compositeDisposable.add(dao.add(movie)
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    fun clear(){
        compositeDisposable.clear()
        Log.d("TAG", "clear: ")
    }
}