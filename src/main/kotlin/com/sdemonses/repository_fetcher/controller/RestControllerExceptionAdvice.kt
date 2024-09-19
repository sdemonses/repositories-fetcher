package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.controller.model.ErrorListResponse
import com.sdemonses.repository_fetcher.controller.model.ErrorResponse
import com.sdemonses.repository_fetcher.controller.model.SimpleErrorResponse
import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException
import com.sdemonses.repository_fetcher.exception.GithubUserNotFoundException
import com.sdemonses.repository_fetcher.util.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllerExceptionAdvice {

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        e: ConstraintViolationException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val errors = e.constraintViolations.map { it.message }
        return mapResponse(ErrorListResponse(400, errors), e)
    }

    @ExceptionHandler(GithubUserNotFoundException::class)
    fun handleGithubUserNotFoundException(
        e: GithubUserNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> =
        mapResponse(SimpleErrorResponse(e.message, 404), e)

    @ExceptionHandler(AcceptHeaderNotValidException::class)
    fun handleGithubUserNotFoundException(
        e: AcceptHeaderNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> =
        mapResponse(SimpleErrorResponse(e.message, 406), e)

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(
        e: Throwable,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> =
        mapResponse(SimpleErrorResponse(e.message, 500), e)

    private fun mapResponse(error: ErrorResponse, exception: Throwable): ResponseEntity<ErrorResponse> {
        when (error.status) {
            in 0..399 -> log.info(exception.message, exception)
            in 400..499 -> log.warn(exception.message, exception)
            else -> log.error(exception.message, exception)
        }

        return ResponseEntity(error, HttpStatusCode.valueOf(error.status))
    }

    companion object {
        private val log = logger()
    }
}
