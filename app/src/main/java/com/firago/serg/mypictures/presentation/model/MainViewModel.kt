package com.firago.serg.mypictures.presentation.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.firago.serg.mypictures.GlobalParameters
import com.firago.serg.mypictures.domain.*
import kotlinx.coroutines.experimental.async


enum class State {
    NORMAL, ERROR, LOADING
}

class MainViewModel(private val globalParameters: GlobalParameters, private val dispatchers: ApplicationDispatchers) : ViewModel() {

    private val links: MutableLiveData<List<PictureLink>> by lazy { MutableLiveData<List<PictureLink>>() }
    private val typeLinks: MutableLiveData<TypeLoading> by lazy { MutableLiveData<TypeLoading>() }
    private val state: MutableLiveData<State> by lazy { MutableLiveData<State>() }

    // use if state.ERROR
    var error: String = ""
        private set
    // if "open picture" event
    private val loadPictureUrl: SingleLiveEvent<PictureLink> by lazy { SingleLiveEvent<PictureLink>() }


    val totalLink: Int
        get() = links.value?.size ?: 0

    var repository
        get() = globalParameters.currentRepository
        set(value) {
            globalParameters.currentRepository = value
        }

    init {
        loadLinks()
    }

    private fun getLinksFromRepository(f: Repository.() -> ResourceLinks) =
            async(dispatchers.uiContext) {
                val resource = async(dispatchers.bgContext) {
                    repository.f()
                }.await()
                if (resource.isSuccessful()) {
                    setNormalState(resource)
                } else {
                    setErrorState(resource)
                }
            }

    fun loadLinks() = getLinksFromRepository { getAllLinks() }
    fun initRepository() = getLinksFromRepository { loadFirstLinks() }
    fun loadMore() = getLinksFromRepository { loadMoreLinks() }


    fun getLinks(): LiveData<List<PictureLink>> = links
    fun getState(): LiveData<State> = state
    fun getTypeLinks(): LiveData<TypeLoading> = typeLinks
    fun openUrlPicture(): LiveData<PictureLink> = loadPictureUrl

    fun showPicture(pictureLink: PictureLink) {
        loadPictureUrl.value = pictureLink
    }

    private fun setNormalState(resource: ResourceLinks) {
        links.value = resource.links.pictures.toList()
        typeLinks.value = resource.links.type
        state.value = State.NORMAL
        error = ""
    }

    private fun setErrorState(resource: ResourceLinks) {
        error = resource.error.message!!
        state.value = State.ERROR
    }

}

