package com.firago.serg.mypictures.domain

data class ErrorObject(val code: Int, val message: String?)
enum class TypeLoading {
    FROM_DISK, FROM_NET
}

data class ResourceLinks(val links: TypeLoadingWrapper, val error: ErrorObject)


data class TypeLoadingWrapper(val type: TypeLoading, val pictures: List<PictureLink>)

fun ResourceLinks.isSuccessful(): Boolean = error == NoError

val NoError: ErrorObject by lazy { ErrorObject(0, null) }

