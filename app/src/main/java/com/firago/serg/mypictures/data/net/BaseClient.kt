package com.firago.serg.mypictures.data.net

import com.firago.serg.mypictures.data.PictureCloudClient
import com.firago.serg.mypictures.data.PicturesResponse
import okhttp3.OkHttpClient
import okhttp3.Request

abstract class BaseClient(private val httpClient: OkHttpClient, val firstPage: String) : PictureCloudClient {
    fun getRequest(url: String): Request {
        return Request
                .Builder()
                .url(url)
                .build()
    }

    fun getResponse(request: Request): PicturesResponse {
        val response = httpClient.newCall(request).execute()
        return if (response.isSuccessful) {
            PicturesResponse(response.code(), response.body()!!.string())
        } else {
            PicturesResponse(response.code(), null)
        }
    }

    override fun getPicturesResponse(): PicturesResponse = getResponse(getRequest(firstPage))
}