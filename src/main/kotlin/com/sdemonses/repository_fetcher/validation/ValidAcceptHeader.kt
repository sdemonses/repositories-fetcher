package com.sdemonses.repository_fetcher.validation

import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException
import org.springframework.http.MediaType


fun validateAcceptHeader(header: String) {
    if (header != ALLOWED_ACCEPT_VALUE) {
        throw AcceptHeaderNotValidException()
    }
}

private const val ALLOWED_ACCEPT_VALUE = MediaType.APPLICATION_JSON_VALUE

