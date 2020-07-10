package kg.amangram.movieapp.data.api

import io.reactivex.Single
import kg.amangram.movieapp.data.api.Constants.Companion.API_KEY
import kg.amangram.movieapp.data.model.MovieById
import kg.amangram.movieapp.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<Response<MovieResponse>>

    @GET("search/movie")
    fun searchMovie(
        @Query("page") page: Int,
        @Query("query") text: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<Response<MovieResponse>>

    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<Response<MovieById>>
}