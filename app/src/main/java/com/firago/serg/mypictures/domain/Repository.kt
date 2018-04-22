package com.firago.serg.mypictures.domain

interface Repository {
    fun loadFirstLinks(): ResourceLinks
    fun loadMoreLinks(): ResourceLinks
    fun getAllLinks(): ResourceLinks

}