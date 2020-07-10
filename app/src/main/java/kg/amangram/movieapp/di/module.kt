package kg.amangram.movieapp.di

import android.app.Application
import androidx.room.Room
import kg.amangram.movieapp.data.api.MovieApi
import kg.amangram.movieapp.data.api.RetrofitService
import kg.amangram.movieapp.data.datasource.DataSourceFactory
import kg.amangram.movieapp.data.datasource.MovieDataSource
import kg.amangram.movieapp.data.local.FavoritesDB
import kg.amangram.movieapp.data.repository.FavoritesRepository
import kg.amangram.movieapp.data.repository.MovieRepository
import kg.amangram.movieapp.ui.detail.DetailViewModel
import kg.amangram.movieapp.ui.main.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    // DB
    fun provideDB(application: Application): FavoritesDB{
        return Room.databaseBuilder(application,FavoritesDB::class.java,"favorites").build()
    }
    fun provideDAO(db: FavoritesDB)= db.getFavoritesDAO()
    single { provideDB(androidApplication()) }
    single { provideDAO(get()) }

    // repository
    single {
        MovieRepository(RetrofitService.buildService(MovieApi::class.java))
    }
    single { FavoritesRepository(get()) }

    single { MovieDataSource(get()) }
    single { DataSourceFactory(get()) }

    // viewModel
    viewModel { MainViewModel(get()) }
    viewModel { (id: Int)-> DetailViewModel(id,get(),get()) }
}