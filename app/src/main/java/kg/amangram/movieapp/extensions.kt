package kg.amangram.movieapp

import android.app.Activity
import android.view.View
import android.widget.Toast

fun View.gone() {
    this.visibility = View.GONE
}
fun View.visible(){
    this.visibility = View.VISIBLE
}
fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}