package com.firago.serg.mypictures.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.firago.serg.mypictures.R
import com.firago.serg.mypictures.domain.PictureLink
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_picture.*
import java.lang.Exception

private const val EXTRA_SAVE_FAVORITE = "FAVORITE_BUNDLE"

class PictureActivity : AppCompatActivity() {

    companion object {
        const val RESULT_FAVORITE = 1000
        const val RESULT_UNFAVORITE = 1001
        private const val EXTRA_URL = "URL_PICTURE"
        private const val EXTRA_FILE_NAME = "FILE_NAME"
        private const val EXTRA_PREVIEW = "URL_PREVIEW"
        private const val EXTRA_ID = "ID_LINK"
        private const val EXTRA_IS_FAVORITE = "FAVORITE"


        private fun Intent.putPicture(link: PictureLink, isFavorite: Boolean) {
            putExtra(EXTRA_URL, link.linkToFile)
            putExtra(EXTRA_FILE_NAME, link.filename)
            putExtra(EXTRA_PREVIEW, link.linkToPreview)
            putExtra(EXTRA_ID, link.id)
            putExtra(EXTRA_IS_FAVORITE, isFavorite)
        }

        @JvmStatic
        fun newIntent(context: Context, link: PictureLink, isFavorite: Boolean): Intent {
            val intent = Intent(context, PictureActivity::class.java)
            intent.putPicture(link, isFavorite)
            return intent
        }

        @JvmStatic
        fun newIntent(link: PictureLink, isFavorite: Boolean): Intent {
            val intent = Intent()
            intent.putPicture(link, isFavorite)
            return intent
        }

        @JvmStatic
        fun getPictureLink(intent: Intent): PictureLink {
            val url = intent.getStringExtra(EXTRA_URL)
            val id = intent.getStringExtra(EXTRA_ID)
            val name = intent.getStringExtra(EXTRA_FILE_NAME)
            val preview = intent.getStringExtra(EXTRA_PREVIEW)
            return PictureLink(id, name, url, preview)
        }
    }

    private lateinit var scaleType: ImageView.ScaleType
    private lateinit var picture: PictureLink
    private var shareActionProvider: ShareActionProvider? = null
    private lateinit var itemFavorite: MenuItem
    private lateinit var itemUnfavorite: MenuItem

    private val picassoCallback: Callback = object : Callback {
        override fun onSuccess() {
            showPicture()
        }

        override fun onError(e: Exception?) {
            val message = e?.toString() ?: getString(R.string.unknown_error_picasso)
            showError(getString(R.string.loading_error_picasso, message))
            //load preview from cache
            Picasso.get()
                    .load(picture.linkToPreview)
                    .placeholder(R.drawable.ic_download_black_300dp)
                    .resize(300, 300)
                    .centerCrop()
                    .into(imageViewPreview)
        }

    }


    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        picture = getPictureLink(intent)

        loadIsFavorite(savedInstanceState)

        showProgress()
        loadImage()

        scaleType = imageViewBigPicture.scaleType
        imageViewBigPicture.setOnClickListener {
            resetScaleType()
        }
        setupActionBar()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_set_favorite -> {
                invisibleFavoriteMenuItem()
                isFavorite = true
                setResult(RESULT_FAVORITE, newIntent(picture, isFavorite))
            }
            R.id.action_unset_favorite -> {
                visibleFavoriteMenuItem()
                setFavorite(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setFavorite(value: Boolean) {
        isFavorite = value
        val result = if (isFavorite) RESULT_FAVORITE else RESULT_UNFAVORITE
        setResult(result, newIntent(picture, isFavorite))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.picture_menu, menu)
        val item = menu?.findItem(R.id.action_share)
        shareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        setShareIntent(createShareIntent())
        itemFavorite = menu!!.findItem(R.id.action_set_favorite)
        itemUnfavorite = menu.findItem(R.id.action_unset_favorite)
        itemFavorite.isVisible = !isFavorite
        itemUnfavorite.isVisible = isFavorite
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(EXTRA_SAVE_FAVORITE, isFavorite)
        super.onSaveInstanceState(outState)
    }


    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun loadIsFavorite(savedInstanceState: Bundle?) {
        var favorite = intent.getBooleanExtra(EXTRA_IS_FAVORITE, false)
        savedInstanceState?.let {
            favorite = it.getBoolean(EXTRA_SAVE_FAVORITE)
        }
        setFavorite(favorite)
    }

    // Call to update the share intent
    private fun setShareIntent(shareIntent: Intent) {
        shareActionProvider?.setShareIntent(shareIntent)
    }

    private fun createShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                picture.linkToFile)
        return shareIntent
    }


    private fun loadImage() {
        Picasso.get()
                .load(picture.linkToFile)
                .error(R.drawable.ic_mood_black_48dp)
                .into(imageViewBigPicture, picassoCallback)
    }

    private fun resetScaleType() {
        imageViewBigPicture.scaleType =
                if (imageViewBigPicture.scaleType != ImageView.ScaleType.CENTER_CROP) {
                    ImageView.ScaleType.CENTER_CROP
                } else {
                    scaleType
                }
    }

    private fun showPicture() {
        errorLauout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        pictureLauout.visibility = View.VISIBLE

    }

    private fun showError(message: String) {
        errorLauout.visibility = View.VISIBLE
        loadingLayout.visibility = View.GONE
        pictureLauout.visibility = View.GONE
        tvErrorMessage.text = message
    }

    private fun showProgress() {
        errorLauout.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
        pictureLauout.visibility = View.GONE
    }

    private fun visibleFavoriteMenuItem() {
        itemFavorite.isVisible = true
        itemUnfavorite.isVisible = false
    }

    private fun invisibleFavoriteMenuItem() {
        itemFavorite.isVisible = false
        itemUnfavorite.isVisible = true
    }
}
