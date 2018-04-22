package com.firago.serg.mypictures.presentation.ui.main


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.firago.serg.mypictures.App
import com.firago.serg.mypictures.R
import com.firago.serg.mypictures.domain.PictureLink
import com.firago.serg.mypictures.domain.TypeLoading
import com.firago.serg.mypictures.presentation.model.AppDispatchers
import com.firago.serg.mypictures.presentation.model.MainViewModel
import com.firago.serg.mypictures.presentation.model.MainViewModelFactory
import com.firago.serg.mypictures.presentation.model.State
import com.firago.serg.mypictures.presentation.ui.PictureActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val PICTURE_WIDTH = 300
private const val PICTURE_ACTIVITY_CODE: Int = 1000


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "MainActivity"

    private lateinit var model: MainViewModel
    private lateinit var adapter: PicturesAdapter
    //for load more picture
    //recyclerview scrolling state
    private var isRecyclerScrolling: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val factory = MainViewModelFactory(App.parameters, AppDispatchers)
        model = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        adapter = PicturesAdapter()
        adapter.setOnItemClickListener { pictureLink ->
            model.showPicture(pictureLink)
        }
        observePictureList()
        observeShowBigPicture()
        observeState()
        observeTypeLoading()

        setupRecyclerView()
        setupBottomNavigationView()
        swipeRefresh.setOnRefreshListener {
            //no showLoading()
            //autoshow refresh
            model.initRepository()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fun changeFavorite(f: (PictureLink) -> Unit) {
            val picture = PictureActivity.getPictureLink(data!!)
            f(picture)
            if (model.repository == App.repositoryFavorite) {
                loadLinks()
            }
        }

        if (requestCode == PICTURE_ACTIVITY_CODE) {

            if (resultCode == PictureActivity.RESULT_FAVORITE) {
                Log.d(LOG_TAG, "favorite")
                changeFavorite { App.setFavorite(it) }
            }
            if (resultCode == PictureActivity.RESULT_UNFAVORITE) {
                Log.d(LOG_TAG, "unfavorite")
                changeFavorite { App.unFavourite(it) }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun setupRecyclerView() {
        recyclerPictures.adapter = adapter
        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerPictures.layoutManager = gridLayoutManager
        setGridSpanCount()
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val itemCount = recyclerView!!.layoutManager.itemCount
                val childCount = recyclerView.childCount
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()

                if (!isRecyclerScrolling && firstVisibleItemPosition + 3 * childCount > itemCount) {
                    isRecyclerScrolling = true
                    model.loadMore()
                }
            }
        }
        recyclerPictures.addOnScrollListener(listener)
    }

    private fun setGridSpanCount() {
        // calc and set spanCount in grid. spanCount should > = 2
        recyclerPictures.viewTreeObserver.addOnGlobalLayoutListener {
            val spanCount = maxOf(2, recyclerPictures.width / PICTURE_WIDTH)
            val gridLayoutManager = recyclerPictures.layoutManager as GridLayoutManager
            gridLayoutManager.spanCount = spanCount
        }
    }

    private fun observeTypeLoading() {
        model.getTypeLinks().observe(this, Observer { type ->
            when (type) {
                TypeLoading.FROM_DISK -> tvMessage.text = getString(R.string.loading_from_disk, model.totalLink)
                TypeLoading.FROM_NET -> tvMessage.text = getString(R.string.loading_from_net, model.totalLink)
            }
        })
    }

    private fun observeState() {
        model.getState().observe(this, Observer {
            when (it) {
                State.LOADING -> showLoading()
                State.ERROR -> {
                    showError(model.error + " " + getString(R.string.error_loading))
                }
                State.NORMAL -> showRecyclerView()
            }
        })
    }


    private fun observeShowBigPicture() {
        model.openUrlPicture().observe(this, Observer { link ->
            val intent = PictureActivity.newIntent(this, link!!, App.isFavourite(link))
            startActivityForResult(intent, PICTURE_ACTIVITY_CODE)
        })
    }

    private fun observePictureList() {
        model.getLinks().observe(this, Observer {
            //            if (adapter.data?.equals(it) == true) return@Observer
            adapter.swapData(it, true)
            showRecyclerView()
            Log.d(LOG_TAG, "$it")
            isRecyclerScrolling = false
            swipeRefresh.isRefreshing = false
        })
    }

    private fun setupBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            val oldRepository = model.repository
            when (item.itemId) {
                R.id.action_yandex -> {
                    model.repository = App.repositoryYandex
                }
                R.id.action_flickr -> {
                    model.repository = App.repositoryFlickr
                }
                R.id.action_favorites -> {
                    model.repository = App.repositoryFavorite
                }
            }
            if (oldRepository != model.repository) {
                adapter.swapData(null, false) // disable blinking
                showLoading()
                loadLinks()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun loadLinks() {
        model.loadLinks()
    }

    private fun showLoading() {
        swipeRefresh.visibility = View.INVISIBLE
        tvLoading.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        tvErrorMessage.visibility = View.GONE
    }

    private fun showRecyclerView() {
        Log.d(LOG_TAG, "show recycler")
        swipeRefresh.visibility = View.VISIBLE
        tvLoading.visibility = View.GONE
        progressBar.visibility = View.GONE
        tvErrorMessage.visibility = View.GONE
    }

    private fun showError(message: String) {
        Log.d(LOG_TAG, "show error = $message")
        swipeRefresh.visibility = View.INVISIBLE
        tvLoading.visibility = View.GONE
        progressBar.visibility = View.GONE
        tvErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = message
    }

}


