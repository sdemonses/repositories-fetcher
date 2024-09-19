package com.sdemonses.repository_fetcher.exception

class AcceptHeaderNotValidException : RuntimeException(ACCEPT_HEADER_ERROR_MESSAGE) {
    companion object {
        const val ACCEPT_HEADER_ERROR_MESSAGE = "Invalid accept header!"
    }
}