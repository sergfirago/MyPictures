package com.firago.serg.mypictures.data.net

import com.firago.serg.mypictures.data.LocalStorage
import com.firago.serg.mypictures.domain.*
import java.io.IOException

class FavouriteRepository(private val storage: LocalStorage) : Repository {
    override fun getAllLinks(): ResourceLinks = loadFirstLinks()

    private var map: MutableMap<String, PictureLink> = storage.loadLinkMap()

    override fun loadFirstLinks(): ResourceLinks {
        val list = map.values.toList()
        return ResourceLinks(TypeLoadingWrapper(TypeLoading.FROM_DISK, list), NoError)
    }

    override fun loadMoreLinks(): ResourceLinks = loadFirstLinks()

    fun isFavourite(link: PictureLink): Boolean {
        return link.id in map
    }

    fun setFavourite(link: PictureLink) {
        map[link.id] = link
        storage.saveMapLinks(map)
    }

    fun unFavorite(link: PictureLink) {
        map.remove(link.id)
        storage.saveMapLinks(map)
    }
}

fun LocalStorage.saveMapLinks(map: Map<String, PictureLink>) {
    savePictureLinks(map.values.toList())
}

fun LocalStorage.loadLinkMap(): MutableMap<String, PictureLink> {
    val map = mutableMapOf<String, PictureLink>()
    try {
        loadPictureLinks()
    } catch (e: IOException) {
        // list not found
        emptyList<PictureLink>()
    }.forEach {
        map[it.id] = it
    }
    return map
}