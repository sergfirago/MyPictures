package com.firago.serg.mypictures.data.net

import com.firago.serg.mypictures.data.LocalStorage
import com.firago.serg.mypictures.domain.PictureLink
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavouriteRepositoryTest {
    lateinit var storage: LocalStorage
    lateinit var repository: FavouriteRepository
    @Before
    fun setUp() {
        storage = object : LocalStorage {
            val list = mutableListOf<PictureLink>()
            override fun loadPictureLinks(): List<PictureLink> {
                return list
            }

            override fun savePictureLinks(list: List<PictureLink>) {
                this.list.clear()
                this.list.addAll(list)
            }

        }
        storage.savePictureLinks(list)
        repository = FavouriteRepository(storage)

    }

    val list = listOf<PictureLink>(PictureLink("id", "name", "file", "preview"))

    @Test
    fun `getAllLinks should return list from storage`() {


        val allLinks = repository.getAllLinks()

        assertEquals(list, allLinks.links.pictures)
    }

    @Test
    fun `loadFirstLinks should return list from storage`() {


        val repositoryLinks = repository.loadFirstLinks()

        assertEquals(list, repositoryLinks.links.pictures)
    }

    @Test
    fun `loadMoreLinks should return list from storage`() {


        val repositoryLinks = repository.loadMoreLinks()

        assertEquals(list, repositoryLinks.links.pictures)

    }

    @Test
    fun `isFavourite=true if link in storage`() {
        val pictureLinkFavorite = PictureLink("id", "name", "file", "preview")
        val pictureLinkNotFavorite = PictureLink("id2", "name", "file", "preview")
        storage.savePictureLinks(listOf(pictureLinkFavorite))
        repository = FavouriteRepository(storage)

        assertTrue(repository.isFavourite(pictureLinkFavorite))
        assertFalse(repository.isFavourite(pictureLinkNotFavorite))

    }

    @Test
    fun `setFavourite set isFavorite`() {
        val pictureLink = PictureLink("id2", "name", "file", "preview")

        storage.savePictureLinks(list)
        repository.setFavourite(pictureLink)
        assertTrue(repository.isFavourite(pictureLink))
    }

    @Test
    fun `unFavorite set isFavorite= false`() {
        val pictureLink = PictureLink("id2", "name", "file", "preview")
        storage.savePictureLinks(listOf(pictureLink))
        repository.unFavorite(pictureLink)
        assertFalse(repository.isFavourite(pictureLink))
    }

    @Test
    fun `unFavorite call save`() {
        val pictureLink = PictureLink("id2", "name", "file", "preview")
        storage.savePictureLinks(listOf(pictureLink))
        repository.unFavorite(pictureLink)
    }
}