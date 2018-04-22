package com.firago.serg.mypictures.data.net.flickr

import com.firago.serg.mypictures.data.net.PictureListMapper
import com.firago.serg.mypictures.data.net.ServerReturnErrorException
import com.firago.serg.mypictures.data.net.flickr.pojo.Photo
import com.firago.serg.mypictures.data.net.flickr.pojo.PictureList
import com.firago.serg.mypictures.domain.PictureLink
import com.google.gson.Gson

class FlickrPictureListMapper : PictureListMapper {
    override fun getPictureList(body: String?): List<PictureLink> {
        val gson = Gson()
        val pictureList = gson.fromJson(body, PictureList::class.java) ?: return emptyList()

        if (pictureList.stat != "ok") {
            throw ServerReturnErrorException(pictureList.message)
        }
        val photoList = pictureList.photos.photo
        val pictureLinks = mutableListOf<PictureLink>()
        photoList.forEach {
            pictureLinks.add(getPictureLink(it))
        }
        return pictureLinks
    }

    private fun getPictureLink(photo: Photo): PictureLink {
        val name = photo.title
        val file = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"
        val preview = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_m.jpg"
        return PictureLink(file, name, file, preview)
    }
}