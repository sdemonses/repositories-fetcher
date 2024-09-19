package com.sdemonses.repository_fetcher.validation

import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType

class ValidAcceptHeaderKtTest {

    @Test
    fun `json accept header should be valid`() {
        validateAcceptHeader(MediaType.APPLICATION_JSON_VALUE)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            MediaType.TEXT_XML_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            MediaType.ALL_VALUE,
            "some value",
            ""
        ]
    )
    fun `not json accept header should be not valid`(value: String) {
        assertThrows<AcceptHeaderNotValidException> {
            validateAcceptHeader(value)
        }
    }
}