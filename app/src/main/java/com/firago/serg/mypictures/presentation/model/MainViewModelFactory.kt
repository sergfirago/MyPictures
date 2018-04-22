package com.firago.serg.mypictures.presentation.model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.firago.serg.mypictures.GlobalParameters

class MainViewModelFactory(private val globalParameters: GlobalParameters, private val cc: ApplicationDispatchers) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
                .getConstructor(GlobalParameters::class.java, ApplicationDispatchers::class.java)
                .newInstance(globalParameters, cc)
    }
}