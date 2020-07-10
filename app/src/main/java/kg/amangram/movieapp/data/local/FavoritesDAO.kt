package kg.amangram.movieapp.data.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import kg.amangram.movieapp.data.model.MovieById

@Dao
interface FavoritesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(movie: MovieById): Completable

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Single<List<MovieById>>

    @Delete
    fun delete(movie: MovieById): Completable
}