package kg.amangram.movieapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kg.amangram.movieapp.R
import kg.amangram.movieapp.data.model.State
import kg.amangram.movieapp.gone
import kg.amangram.movieapp.toast
import kg.amangram.movieapp.ui.detail.DetailActivity
import kg.amangram.movieapp.visible
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MenuItem.OnActionExpandListener,
    SearchView.OnQueryTextListener {

    private val TAG = this::class.java.simpleName
    private val viewModel: MainViewModel by viewModel()
    private lateinit var movieAdapter: MovieAdapter
    private var spanCount = 1
    private var isConnected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAdapter()
        setData()
    }

    private fun setAdapter() {
        movieAdapter = MovieAdapter {movieId->
            startActivity(Intent(this,DetailActivity::class.java)
                .putExtra("MOVIE_ID",movieId))
        }
        var orientation = resources.configuration.orientation
        spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            2
        else
            1
        rv_movies.apply {
            adapter = movieAdapter
            layoutManager =
                GridLayoutManager(this@MainActivity, spanCount, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun setData() {
        viewModel.movieList.observe(this, Observer { movies ->
            movieAdapter.submitList(movies)
            connectionListener()
        })

    }

    private fun connectionListener() {
        viewModel.getState()?.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> pb_main.visible()
                is State.Success -> {
                    pb_main.gone()
                    isConnected = true
                    tv_no_connection.gone()
                }
                is State.Failed -> {
                    isConnected = false
                    pb_main.gone()
                    tv_no_connection.visible()
                    Log.e(TAG, state.message)
                    toast(getString(R.string.connection_error))
                }
            }
        })
    }

    private fun showAll(){
        viewModel.reloadList(this,null)
        setData()
        tv_no_matches.gone()
        rv_movies.visible()
    }

    private fun searchStateListener(){
        viewModel.getSearchSize()?.observe(this, Observer { size->
            if (size>0){
                tv_no_matches.gone()
                rv_movies.visible()
            } else{
                rv_movies.gone()
                tv_no_matches.visible()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.search)
        search?.setOnActionExpandListener(this)
        val searchView = search?.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        showAll()
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()&&isConnected){
            viewModel.reloadList(this,newText)
            setData()
            searchStateListener()
        }
       return true
    }
}