package kg.amangram.movieapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kg.amangram.movieapp.data.model.MovieById
import kg.amangram.movieapp.data.model.State
import kg.amangram.movieapp.data.repository.FavoritesRepository
import kg.amangram.movieapp.data.repository.MovieRepository

class DetailViewModel(
    private val id: Int,
    private val repository: MovieRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _movie = repository.getMovieById(id)
    val movie: LiveData<State<MovieById>>
        get() = _movie

    private val _favorites = favoritesRepository.getFavorites()
    val favorites: LiveData<List<MovieById>>
    get() = _favorites

    fun addMovie(){
        when(_movie.value){
            is State.Success->{
                favoritesRepository.addMovie((_movie.value as State.Success<MovieById>).data)
            }
        }
    }

    fun deleteMovie(){
        when(_movie.value){
            is State.Success-> favoritesRepository.deleteMovie((_movie.value as State.Success<MovieById>).data)
        }
    }

    override fun onCleared() {
        favoritesRepository.clear()
        super.onCleared()
    }
}