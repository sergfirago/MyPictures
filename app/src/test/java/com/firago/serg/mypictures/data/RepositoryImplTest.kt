package com.firago.serg.mypictures.data

import com.firago.serg.mypictures.data.net.*
import com.firago.serg.mypictures.domain.ErrorObject
import com.firago.serg.mypictures.domain.NoError
import com.firago.serg.mypictures.domain.PictureLink
import com.firago.serg.mypictures.domain.TypeLoading
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class RepositoryImplTest {
    val list = listOf(PictureLink("a", "b", "c", "d"))
    val listNext = listOf(PictureLink("nexta", "nextb", "nextc", "nextd"))
    val body = "body"

    @Test
    fun `loadFirstLinks should return NoError if HttpCode = 200`() {
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(200, body)
        }
        val storage = mock<LocalStorage>()

        val mapper = mock<PictureListMapper> {
            on { getPictureList(body) } doReturn list
        }

        val repository = RepositoryImpl(client, storage, mapper)
        val resource = repository.loadFirstLinks()

        assertEquals(NoError, resource.error)
    }

    @Test
    fun `loadFirstLinks should call storage save if HttpCode = 200`() {
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(200, body)
        }
        val mapper = mock<PictureListMapper> {
            on { getPictureList(any()) } doReturn list
        }
        val storage = mock<LocalStorage>()

        val repository = RepositoryImpl(client, storage, mapper)
        repository.loadFirstLinks()

        verify(storage).savePictureLinks(list)
    }

    @Test
    fun `loadFirstLinks should return type = FROM_NET if HttpCode = 200`() {
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(200, body)
        }
        val mapper = mock<PictureListMapper> {
            on { getPictureList(any()) } doReturn list
        }
        val storage = mock<LocalStorage>()

        val repository = RepositoryImpl(client, storage, mapper)
        val resource = repository.loadFirstLinks()

        assertEquals(TypeLoading.FROM_NET, resource.links.type)
    }

    @Test
    fun `loadFirstLinks should return empty list if HttpError`() {
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(404, body)
        }
        val mapper = mock<PictureListMapper> {
            on { getPictureList(body) } doReturn list
        }
        val storage = mock<LocalStorage>()


        val repository = RepositoryImpl(client, storage, mapper)
        val resource = repository.loadFirstLinks()

        assertEquals(ErrorObject(404, "Error"), resource.error)
        assertEquals(TypeLoading.FROM_NET, resource.links.type)
        assertEquals(emptyList<PictureLink>(), resource.links.pictures)
        verify(storage, never()).savePictureLinks(any())

    }

    @Test
    fun `loadFirstLinks load list from storage if offline`() {
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doThrow (IOException())
        }
        val mapper = mock<PictureListMapper>()
        val storage = mock<LocalStorage> {
            on { loadPictureLinks() } doReturn list
        }

        val repository = RepositoryImpl(client, storage, mapper)
        val resource = repository.loadFirstLinks()

        assertEquals(NoError, resource.error)
        assertEquals(TypeLoading.FROM_DISK, resource.links.type)
        assertEquals(list, resource.links.pictures)
        verify(mapper, never()).getPictureList(any())
    }

    @Test
    fun `loadFirstLinks return error if server api return error`() {
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(200, body)
        }
        val mapper = mock<PictureListMapper>() {
            on { getPictureList(any()) } doThrow (ServerReturnErrorException("error message"))
        }
        val storage = mock<LocalStorage>()

        val repository = RepositoryImpl(client, storage, mapper)
        val resource = repository.loadFirstLinks()

        assertEquals("error message", resource.error.message)
        assertEquals(ERROR_SERVER, resource.error.code)
        verify(storage, never()).loadPictureLinks()
    }

    @Test
    fun `loadFirstLinks return error if offline and storage empty`() {
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doThrow (IOException("error message"))
        }
        val mapper = mock<PictureListMapper>()
        val storage = mock<LocalStorage> {
            on { loadPictureLinks() } doThrow IOException("storage error")
        }

        val repository = RepositoryImpl(client, storage, mapper)
        val resource = repository.loadFirstLinks()

        assertEquals("storage error", resource.error.message)
        assertEquals(ERROR_STORAGE, resource.error.code)
        verify(storage).loadPictureLinks()
    }

    @Test
    fun `loadMoreLinks should return two list if connect`() {
        val BODY_FIRST_RESPONSE = "body"
        val BODY_MORE_RESPONSE = "body2"
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(200, BODY_FIRST_RESPONSE)
            on { getMorePictures() } doReturn PicturesResponse(200, BODY_MORE_RESPONSE)
        }
        val mapper = mock<PictureListMapper> {
            on { getPictureList(BODY_FIRST_RESPONSE) } doReturn list
            on { getPictureList(BODY_MORE_RESPONSE) } doReturn listNext
        }
        val storage = mock<LocalStorage>()


        val repository = RepositoryImpl(client, storage, mapper)
        repository.loadFirstLinks()
        val resource = repository.loadMoreLinks()


        assertEquals(TypeLoading.FROM_NET, resource.links.type)
        assertEquals(list + listNext, resource.links.pictures)
    }

    @Test
    fun `loadMoreLinks should return first list if HttpCode==404`() {
        val BODY_FIRST_RESPONSE = "body"
        val BODY_MORE_RESPONSE = "body2"
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(200, BODY_FIRST_RESPONSE)
            on { getMorePictures() } doReturn PicturesResponse(404, BODY_MORE_RESPONSE)
        }
        val mapper = mock<PictureListMapper> {
            on { getPictureList(BODY_FIRST_RESPONSE) } doReturn list
            on { getPictureList(BODY_MORE_RESPONSE) } doReturn listNext
        }
        val storage = mock<LocalStorage>()


        val repository = RepositoryImpl(client, storage, mapper)
        repository.loadFirstLinks()
        val resource = repository.loadMoreLinks()


        assertEquals(TypeLoading.FROM_NET, resource.links.type)
        assertEquals(list, resource.links.pictures)
        verify(mapper, never()).getPictureList(BODY_MORE_RESPONSE)
    }

    @Test
    fun `loadMoreLinks should return first list if disconnect`() {
        val BODY_FIRST_RESPONSE = "body"
        val BODY_MORE_RESPONSE = "body2"
        val client = mock<PictureCloudClient> {
            on { getPicturesResponse() } doReturn PicturesResponse(200, BODY_FIRST_RESPONSE)
            on { getMorePictures() } doThrow IOException("error connect")
        }
        val mapper = mock<PictureListMapper> {
            on { getPictureList(BODY_FIRST_RESPONSE) } doReturn list
            on { getPictureList(BODY_MORE_RESPONSE) } doReturn listNext
        }
        val storage = mock<LocalStorage>()


        val repository = RepositoryImpl(client, storage, mapper)
        repository.loadFirstLinks()
        val resource = repository.loadMoreLinks()


        assertEquals(TypeLoading.FROM_NET, resource.links.type)
        assertEquals(list, resource.links.pictures)
        verify(mapper, never()).getPictureList(BODY_MORE_RESPONSE)
    }

}

