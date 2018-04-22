package com.firago.serg.mypictures.data.net.yandex

import com.firago.serg.mypictures.data.net.PictureListMapper
import com.firago.serg.mypictures.data.net.yandex.pojo.ListPictures
import com.firago.serg.mypictures.domain.PictureLink
import com.google.gson.Gson

class YandexPictureListMapper : PictureListMapper {
    override fun getPictureList(body: String?): List<PictureLink> {
        val listItems = Gson().fromJson(body, ListPictures::class.java) ?: return emptyList()
        return getListPicture(listItems)
    }

    private fun getListPicture(listItems: ListPictures) =
            listItems.embedded.items
                    .filter { it.mediaType != null && it.mediaType.contains("image") }
                    .map { PictureLink(it.md5, it.name, it.file, it.preview) }
}