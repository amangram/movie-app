package kg.amangram.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.amangram.movieapp.data.model.MovieById

@Database(entities = [MovieById::class], version = 1)
abstract class FavoritesDB : RoomDatabase() {
    abstract fun getFavoritesDAO(): FavoritesDAO
}