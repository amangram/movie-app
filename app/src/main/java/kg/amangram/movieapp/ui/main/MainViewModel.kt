package kg.amangram.movieapp.ui.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kg.amangram.movieapp.data.api.Constants.Companion.PER_PAGE
import kg.amangram.movieapp.data.datasource.DataSourceFactory
import kg.amangram.movieapp.data.model.Movie

class MainViewModel(
    private val dataSourceFactory: DataSourceFactory
) : ViewModel() {

    var movieList: LiveData<PagedList<Movie>>
    private val config: PagedList.Config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(PER_PAGE)
        .build()

    init {
        dataSourceFactory.setText(null)
        movieList = LivePagedListBuilder(dataSourceFactory, config).build()
    }

    fun reloadList(lifecycleOwner: LifecycleOwner, searchText: String?) {
        movieList.removeObservers(lifecycleOwner)
        dataSourceFactory.setText(searchText)
        movieList = LivePagedListBuilder(dataSourceFactory,config).build()
    }

    fun getState() = dataSourceFactory.getState()

}