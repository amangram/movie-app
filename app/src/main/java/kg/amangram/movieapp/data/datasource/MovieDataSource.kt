package kg.amangram.movieapp.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.amangram.movieapp.data.api.Constants.Companion.FIRST_PAGE
import kg.amangram.movieapp.data.model.Movie
import kg.amangram.movieapp.data.model.State
import kg.amangram.movieapp.data.repository.MovieRepository

class MovieDataSource(private val repository: MovieRepository) : PageKeyedDataSource<Int, Movie>() {
    private val compositeDisposable = CompositeDisposable()
    private var searchText: String? = null
    private val state by lazy { MutableLiveData<State<String>>() }
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        state.postValue(State.loading())
        compositeDisposable.add(
            getApi(FIRST_PAGE)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        response.body()?.results?.let { movieList ->
                            state.postValue(State.success("Success"))
                            callback.onResult(movieList, null, FIRST_PAGE + 1)
                        }
                    }
                }, {
                    it.printStackTrace()
                    state.postValue(State.failed(it.localizedMessage))
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        state.postValue(State.loading())
        compositeDisposable.add(
            getApi(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        state.postValue(State.success("Success"))
                        response.body()?.results?.let { movieList ->
                            val key = params.key + 1
                            callback.onResult(movieList, key)
                        }
                    }
                }, {
                    it.printStackTrace()
                    state.postValue(State.failed(it.localizedMessage))
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        compositeDisposable.add(
            getApi(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        response.body()?.results?.let { movieList ->
                            val key = if (params.key > 1) params.key - 1 else 0
                            callback.onResult(movieList, key)
                        }
                    }
                }, {})
        )
    }

    fun setSearchText(text: String?) {
            searchText = text
    }

    fun getState() = state as LiveData<State<String>>

    private fun getApi(page: Int) =
        if (searchText.isNullOrEmpty()) repository.getMovies(page) else repository.search(
            page,
            searchText!!
        )
}