package com.firago.serg.mypictures.data

import java.io.IOException

data class PicturesResponse(val code: Int, val body: String?)

fun PicturesResponse.isSuccessful(): Boolean = code in 200..299

interface PictureCloudClient {
    @Throws(IOException::class)
    fun getPicturesResponse(): PicturesResponse

    @Throws(IOException::class)
    fun getMorePictures(page: Int): PicturesResponse
}