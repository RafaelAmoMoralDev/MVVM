package com.example.mvvm.api

sealed class ApiResponse<T> {


}

data class ApiSuccess<T>(val body: T, val links: Map<String, String>) : ApiResponse<T>() {
    constructor(body: T, linksHeaders: String?) : this(
        body = body,
        links = linksHeaders?.extractLinks() ?: emptyMap()
    )
}