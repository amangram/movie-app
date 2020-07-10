package kg.amangram.movieapp.data.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kg.amangram.movieapp.data.model.Movie

class DataSourceFactory(private val dataSource: MovieDataSource): DataSource.Factory<Int, Movie>() {

    private val dataSourceLiveData = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun getDataSource()= dataSourceLiveData.value

    fun setText(text: String?){
        getDataSource()?.setSearchText(text)
    }

    fun getState() = getDataSource()?.getState()

    fun getSearchSize() = getDataSource()?.getSearchSize()
}