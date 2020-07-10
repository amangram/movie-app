package kg.amangram.movieapp.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites"
)
data class MovieById(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val overview: String,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String
)