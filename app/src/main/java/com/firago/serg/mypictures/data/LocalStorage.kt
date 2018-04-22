package com.firago.serg.mypictures.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.support.annotation.VisibleForTesting
import com.firago.serg.mypictures.domain.PictureLink
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

//private const val STORAGE_TAG = "Pictures.list"
private const val LIST_TAG = "list"

interface LocalStorage {
    @Throws(IOException::class)
    fun loadPictureLinks(): List<PictureLink>

    fun savePictureLinks(list: List<PictureLink>)
}

class LocalStorageImpl(context: Context, private val storageName: String) : LocalStorage {
    private val preferences = context.getSharedPreferences(storageName, MODE_PRIVATE)

    override fun loadPictureLinks(): List<PictureLink> {
        val gson = Gson()
        val json = preferences.getString(LIST_TAG, "")
        if (json == "") throw IOException("$storageName:$LIST_TAG not found")
        val list = gson.fromJson<List<PictureLink>>(json, object : TypeToken<List<PictureLink>>() {}.type)

        return list

    }

    override fun savePictureLinks(list: List<PictureLink>) {
        val json = Gson().toJson(list)
        preferences.edit().putString(LIST_TAG, json).apply()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun clear() {
        preferences.edit().remove(LIST_TAG).apply()
    }
}