package com.firago.serg.mypictures.presentation.model

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.firago.serg.mypictures.GlobalParameters
import com.firago.serg.mypictures.domain.*
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

object TestApplicationDispatchers : ApplicationDispatchers {
    override val uiContext = Unconfined
    override val bgContext = Unconfined
}

class GlobalParametersStub(override var currentRepository: Repository) : GlobalParameters

fun makeModel(repository: Repository) = MainViewModel(GlobalParametersStub(repository), TestApplicationDispatchers)
class MainViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    val list = TypeLoadingWrapper(TypeLoading.FROM_NET,
            listOf(PictureLink("id", "LIST_TAG", "linkToFile", "linkToPreview")))

    @Test
    fun `success repository_getAllLinks should change links`() {

        val repository = mock<Repository> {
            on { getAllLinks() } doReturn ResourceLinks(list, NoError)
        }
        val observer = mock<Observer<List<PictureLink>>>()

        runBlocking {
            val model = makeModel(repository)
            model.getLinks().observeForever(observer)
        }
        verify(observer).onChanged(list.pictures)
    }

    @Test
    fun `success repository_getAllLinks should change type`() {
        val repository = mock<Repository> {
            on { getAllLinks() } doReturn ResourceLinks(list, NoError)
        }
        val observer = mock<Observer<TypeLoading>>()
        runBlocking {
            val model = makeModel(repository)
            model.getTypeLinks().observeForever(observer)
        }
        verify(observer).onChanged(list.type)
    }

    @Test
    fun `success repository_loadFirstLinks shouldn't send error`() {
        val repository = mock<Repository> {
            on { loadFirstLinks() } doReturn ResourceLinks(list, NoError)
        }

        val observer = mock<Observer<State>>()
        runBlocking {
            val model = makeModel(repository)
            model.getState().observeForever(observer)
        }

        verify(observer, never()).onChanged(State.ERROR)
    }

    @Test
    fun `failed reposytory_loadFirstLinks shouldn't change links`() {
        val repository = mock<Repository> {
            on { loadFirstLinks() } doReturn ResourceLinks(list, ErrorObject(100, "Error"))
        }
        val observer = mock<Observer<List<PictureLink>>>()

        runBlocking {
            val model = makeModel(repository)
            model.getLinks().observeForever(observer)
        }
        verify(observer, never()).onChanged(list.pictures)
    }

    @Test
    fun `failed repository_getAllLinks should send error`() {
        val errorObject = ErrorObject(100, "Error")
        val repository = mock<Repository> {
            on { getAllLinks() } doReturn ResourceLinks(list, errorObject)
        }

        val observer = mock<Observer<State>>()
        runBlocking {
            val model = makeModel(repository)
            model.getState().observeForever(observer)
        }

        verify(observer).onChanged(State.ERROR)
    }

    @Test
    fun `failed repository_getMoreLinks shouldn't send error`() {
        val errorObject = ErrorObject(100, "Error")
        val repository = mock<Repository> {
            on { loadMoreLinks() } doReturn ResourceLinks(list, errorObject)
        }

        val observer = mock<Observer<State>>()
        runBlocking {
            val model = makeModel(repository)
            model.getState().observeForever(observer)
        }

        verify(observer, never()).onChanged(State.ERROR)
    }

    @Test
    fun `success repository_getMoreLinks should return list`() {
        val repository = mock<Repository> {
            on { loadFirstLinks() } doReturn ResourceLinks(list, NoError)
            on { loadMoreLinks() } doReturn ResourceLinks(list, NoError)
        }

        val observer = mock<Observer<List<PictureLink>>>()
        runBlocking {
            val model = makeModel(repository)
            model.loadMore()
            model.getLinks().observeForever(observer)
        }
        verify(observer).onChanged(list.pictures)
    }
}