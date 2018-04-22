package com.firago.serg.mypictures

import android.app.Application
import com.firago.serg.mypictures.data.LocalStorageImpl
import com.firago.serg.mypictures.data.net.FavouriteRepository
import com.firago.serg.mypictures.data.net.RepositoryImpl
import com.firago.serg.mypictures.data.net.flickr.FlickrClient
import com.firago.serg.mypictures.data.net.flickr.FlickrPictureListMapper
import com.firago.serg.mypictures.data.net.yandex.YandexDiskClient
import com.firago.serg.mypictures.data.net.yandex.YandexPictureListMapper
import com.firago.serg.mypictures.domain.PictureLink
import com.firago.serg.mypictures.domain.Repository
import okhttp3.OkHttpClient

const val YANDEX_STORAGE_NAME = "yandex.list"
const val FLICKR_STORAGE_NAME = "flickr.list"
const val FAVORITE_STORAGE_NAME = "favorite.list"

class App : Application() {

    companion object {
        fun setFavorite(pictureLink: PictureLink) {
            repositoryFavorite.setFavourite(pictureLink)
        }

        fun isFavourite(pictureLink: PictureLink) = repositoryFavorite.isFavourite(pictureLink)
        fun unFavourite(pictureLink: PictureLink) = repositoryFavorite.unFavorite(pictureLink)

        lateinit var repositoryFavorite: FavouriteRepository
            private set
        lateinit var repositoryYandex: Repository
            private set
        lateinit var repositoryFlickr: Repository
            private set
        val parameters = object : GlobalParameters {
            override var currentRepository: Repository
                get() = repository
                set(value) {
                    repository = value
                }
            private lateinit var repository: Repository
        }
    }

    override fun onCreate() {
        super.onCreate()
        val httpClient = OkHttpClient()

        repositoryFavorite = FavouriteRepository(LocalStorageImpl(applicationContext, FAVORITE_STORAGE_NAME))
        repositoryYandex = RepositoryImpl(YandexDiskClient(httpClient),
                LocalStorageImpl(applicationContext, YANDEX_STORAGE_NAME),
                YandexPictureListMapper())
        repositoryFlickr = RepositoryImpl(FlickrClient(httpClient),
                LocalStorageImpl(applicationContext, FLICKR_STORAGE_NAME),
                FlickrPictureListMapper())
        parameters.currentRepository = repositoryYandex


    }
}