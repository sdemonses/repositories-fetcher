package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.controller.model.ErrorListResponse
import com.sdemonses.repository_fetcher.controller.model.SimpleErrorResponse
import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException
import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException.Companion.ACCEPT_HEADER_ERROR_MESSAGE
import com.sdemonses.repository_fetcher.exception.GithubUserNotFoundException
import io.mockk.every
import io.mockk.mockk
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class RestControllerExceptionAdviceTest {
    private val exceptionHandler = RestControllerExceptionAdvice()

    @Test
    fun `should handle ConstraintViolationException and return 400 with error messages`() {
        val violation = mockk<ConstraintViolation<*>> {
            every { message } returns "Invalid field"
            every { propertyPath } returns mockk()
        }
        val exception = ConstraintViolationException(setOf(violation))

        val response = exceptionHandler.handleConstraintViolationException(exception, mockk())

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertTrue((response.body!! as ErrorListResponse).messages.contains("Invalid field"))
        assertEquals(400, response.body!!.status)
    }

    @Test
    fun `should handle GithubUserNotFoundException and return 404`() {
        val username = "someone"
        val exception = GithubUserNotFoundException(username)

        val response = exceptionHandler.handleGithubUserNotFoundException(exception, mockk())

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("User: '$username' not found", (response.body!! as SimpleErrorResponse).message)
        assertEquals(404, response.body!!.status)
    }

    @Test
    fun `should handle AcceptHeaderNotValidException and return 406`() {
        val exception = AcceptHeaderNotValidException()

        val response = exceptionHandler.handleGithubUserNotFoundException(exception, mockk())

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.statusCode)
        assertEquals(ACCEPT_HEADER_ERROR_MESSAGE, (response.body!! as SimpleErrorResponse).message)
        assertEquals(406, response.body!!.status)
    }

    @Test
    fun `should handle Throwable and return 500`() {
        val exception = RuntimeException("Unexpected error")

        val response = exceptionHandler.handleThrowable(exception, mockk())

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Unexpected error", (response.body!! as SimpleErrorResponse).message)
        assertEquals(500, response.body!!.status)
    }

}