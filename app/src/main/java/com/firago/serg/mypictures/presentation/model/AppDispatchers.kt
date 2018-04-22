package com.firago.serg.mypictures.presentation.model

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

interface ApplicationDispatchers {
    val uiContext: CoroutineDispatcher
    val bgContext: CoroutineDispatcher
}

object AppDispatchers : ApplicationDispatchers {
    override val uiContext = UI
    private val totalThread = (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)
    override val bgContext = newFixedThreadPoolContext(totalThread, "IO")
}
