package com.firago.serg.mypictures.data

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.firago.serg.mypictures.domain.PictureLink
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LocalStorageImplTest {
    private lateinit var storage: LocalStorageImpl

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        storage = LocalStorageImpl(context, "TEST_STORAGE.list")
    }

    @After
    fun tearDown() {
        storage.clear()
    }

    @Test
    fun savedListShouldBeLoaded() {
        val list = listOf(PictureLink("id", "file", "link", "preview"),
                PictureLink("id2", "file2", "link2", "preview2"))
        storage.savePictureLinks(list)
        val loadList = storage.loadPictureLinks()
        assertEquals(list, loadList)
    }

    @Test(expected = IOException::class)
    fun loadListShouldThrowException() {
        storage.loadPictureLinks()
    }

}