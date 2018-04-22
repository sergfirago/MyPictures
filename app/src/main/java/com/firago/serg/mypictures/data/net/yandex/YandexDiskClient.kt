package com.firago.serg.mypictures.data.net.yandex

import com.firago.serg.mypictures.data.PicturesResponse
import com.firago.serg.mypictures.data.net.BaseClient
import okhttp3.OkHttpClient

val YANDEX_URL = "https://cloud-api.yandex.net:443/v1/disk/public/resources?public_key=https%3A%2F%2Fyadi.sk%2Fd%2F4MgbWI016Iz5u&limit=500&preview_crop=false&preview_size=300x300"

class YandexDiskClient(httpClient: OkHttpClient) : BaseClient(httpClient, YANDEX_URL) {
    override fun getMorePictures(): PicturesResponse {
        return PicturesResponse(200, "")
    }
}
