package com.sdemonses.repository_fetcher.controller.model

sealed class ErrorResponse(
    open val status: Int
)

data class SimpleErrorResponse(
    override val status: Int,
    val message: String,
) : ErrorResponse(status) {
    constructor(message: String?, status: Int) : this(status, message ?: "something going wrong")
}

data class ErrorListResponse(
    override val status: Int,
    val messages: List<String>,
) : ErrorResponse(status)