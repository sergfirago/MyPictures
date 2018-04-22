package com.firago.serg.mypictures.data.net.flickr

import com.firago.serg.mypictures.data.PicturesResponse
import com.firago.serg.mypictures.data.net.BaseClient
import okhttp3.OkHttpClient

const val API_KEY = "ff49fcd4d4a08aa6aafb6ea3de826464"
//const val URL_PICTURE_LIST = "https://api.flickr.com/services/rest/?method=flickr.photos.search&tag=cat&api_key=$API_KEY&format=json&nojsoncallback=1"
const val URL_PICTURE_LIST = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=$API_KEY&tags=cat&format=json&nojsoncallback=1"
//const val URL_MORE_PICTURE_LIST = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=$API_KEY&format=jsonnojsoncallback=1"

class FlickrClient(httpClient: OkHttpClient) : BaseClient(httpClient, URL_PICTURE_LIST) {
    var page = 1
    override fun getPicturesResponse(): PicturesResponse {
        page = 1
        return super.getPicturesResponse()
    }

    override fun getMorePictures(): PicturesResponse {
        if (page >= 10) return PicturesResponse(200, "")
        page++
        val pageUrl = "$URL_PICTURE_LIST&page=$page"
        return getResponse(getRequest(pageUrl))
    }
}
