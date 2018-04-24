package com.firago.serg.mypictures.data.net.flickr

import com.firago.serg.mypictures.data.PicturesResponse
import com.firago.serg.mypictures.data.net.BaseClient
import okhttp3.OkHttpClient

const val API_KEY = "ff49fcd4d4a08aa6aafb6ea3de826464"
const val URL_PICTURE_LIST = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=$API_KEY&tags=cat&format=json&nojsoncallback=1"
// only 10 pages * 100 photo (default) = 1000
class FlickrClient(httpClient: OkHttpClient) : BaseClient(httpClient, URL_PICTURE_LIST) {
    override fun getMorePictures(page: Int): PicturesResponse {
        if (page >= 10) return PicturesResponse(200, "")
        val pageUrl = "$URL_PICTURE_LIST&page=$page"
        return getResponse(getRequest(pageUrl))
    }
}
