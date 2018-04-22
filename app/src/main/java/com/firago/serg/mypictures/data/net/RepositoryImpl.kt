package com.firago.serg.mypictures.data.net

import com.firago.serg.mypictures.data.LocalStorage
import com.firago.serg.mypictures.data.PictureCloudClient
import com.firago.serg.mypictures.data.isSuccessful
import com.firago.serg.mypictures.domain.*
import java.io.IOException

interface PictureListMapper {
    fun getPictureList(body: String?): List<PictureLink>
}

class ServerReturnErrorException(message: String) : RuntimeException(message)

const val ERROR_SERVER = -1000
const val ERROR_STORAGE = -1
const val UNKNOW_ERROR = -100


class RepositoryImpl(private val client: PictureCloudClient,
                     private val storage: LocalStorage,
                     private val mapper: PictureListMapper) : Repository {

    override fun getAllLinks(): ResourceLinks {
        if (list == null) {
            return loadFirstLinks()
        }
        return ResourceLinks(TypeLoadingWrapper(type, list!!), NoError)
    }

    private val LOG_TAG = "RepositoryTest"
    private var list: MutableList<PictureLink>? = null
    private lateinit var type: TypeLoading
    override fun loadFirstLinks(): ResourceLinks {
        try {
            val response = client.getPicturesResponse()
            return if (response.isSuccessful()) {
                list = mapper.getPictureList(response.body).toMutableList()
                type = TypeLoading.FROM_NET
                storage.savePictureLinks(list!!)
                val loadingList = TypeLoadingWrapper(type, list!!)
                ResourceLinks(loadingList, NoError)
            } else {
                // HTTP CODE != 200
                getResourceLinksWithError(response.code, "Error")
            }
        } catch (e: ServerReturnErrorException) {
            return getResourceLinksWithError(ERROR_SERVER, e.message!!)
        } catch (e: IOException) {
            // no connect or bad json
            return loadFromStorageResourceLinks()
        } catch (e: Exception) {
            return getResourceLinksWithError(UNKNOW_ERROR, e.toString())
        }
    }


    override fun loadMoreLinks(): ResourceLinks {
        try {
            val response = client.getMorePictures()
            if (response.isSuccessful()) {
                list!!.addAll(mapper.getPictureList(response.body))
                storage.savePictureLinks(list!!)
            }
        } catch (e: IOException) {
        }
        return ResourceLinks(TypeLoadingWrapper(type, list!!), NoError)
    }

    private fun getResourceLinksWithError(code: Int, message: String): ResourceLinks {
        val emptyList = TypeLoadingWrapper(TypeLoading.FROM_NET, listOf())
        return ResourceLinks(emptyList, ErrorObject(code, message))
    }

    private fun loadFromStorageResourceLinks(): ResourceLinks {
        return try {
            list = storage.loadPictureLinks().toMutableList()
            type = TypeLoading.FROM_DISK
            val loadingList = TypeLoadingWrapper(type, list!!)
            ResourceLinks(loadingList, NoError)
        } catch (e: IOException) {
            getResourceLinksWithError(ERROR_STORAGE, e.message!!)
        }
    }

}