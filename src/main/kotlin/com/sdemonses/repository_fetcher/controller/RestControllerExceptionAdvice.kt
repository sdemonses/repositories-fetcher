package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.controller.model.ErrorListResponse
import com.sdemonses.repository_fetcher.controller.model.ErrorResponse
import com.sdemonses.repository_fetcher.controller.model.SimpleErrorResponse
import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException
import com.sdemonses.repository_fetcher.exception.GithubUserNotFoundException
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
        return mapResponse(ErrorListResponse(400, errors))
    }

    @ExceptionHandler(GithubUserNotFoundException::class)
    fun handleGithubUserNotFoundException(
        e: GithubUserNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> =
        mapResponse(SimpleErrorResponse(e.message, 404))

    @ExceptionHandler(AcceptHeaderNotValidException::class)
    fun handleGithubUserNotFoundException(
        e: AcceptHeaderNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> =
        mapResponse(SimpleErrorResponse(e.message, 406))

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(
        e: Throwable,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> =
        mapResponse(SimpleErrorResponse(e.message, 500))

    private fun mapResponse(error: ErrorResponse): ResponseEntity<ErrorResponse> =
        ResponseEntity(error, HttpStatusCode.valueOf(error.status))
}
