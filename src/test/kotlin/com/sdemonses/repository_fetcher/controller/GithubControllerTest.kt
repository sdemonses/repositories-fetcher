package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.controller.model.RepositoriesResponse
import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException
import com.sdemonses.repository_fetcher.exception.GithubUserNotFoundException
import com.sdemonses.repository_fetcher.service.GithubService
import com.sdemonses.repository_fetcher.validation.validateAcceptHeader
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GithubControllerTest {
    private val service = mockk<GithubService>()
    private val controller = GithubController(service)

    @Test
    fun `should validate accept header and call github service`() {
        mockkStatic("com.sdemonses.repository_fetcher.validation.ValidAcceptHeaderKt")
        val acceptHeader = "someHeader"
        val userName = "username"
        val page = 1
        val size = 1
        every { validateAcceptHeader(any()) } just Runs
        val response = mockk<RepositoriesResponse>()
        every { service.getAllNonForkRepositories(any(), any(), any()) } returns response

        val result = controller.getNotForkRepositories(acceptHeader, userName, page, size)

        assertEquals(response, result)
        verify(exactly = 1) { validateAcceptHeader(acceptHeader) }
        verify(exactly = 1) { service.getAllNonForkRepositories(userName, page, size) }
        unmockkStatic("com.sdemonses.repository_fetcher.validation.ValidAcceptHeaderKt")
    }

    @Test
    fun `should rethrow exception if header is not valid`() {
        mockkStatic("com.sdemonses.repository_fetcher.validation.ValidAcceptHeaderKt")
        val acceptHeader = "someHeader"
        val userName = "username"
        val page = 1
        val size = 1
        every { validateAcceptHeader(any()) } throws AcceptHeaderNotValidException()

        assertThrows<AcceptHeaderNotValidException>(){ controller.getNotForkRepositories(acceptHeader, userName, page, size) }

        verify(exactly = 1) { validateAcceptHeader(acceptHeader) }
        verify(exactly = 0) { service.getAllNonForkRepositories(any(), any(), any()) }
        unmockkStatic("com.sdemonses.repository_fetcher.validation.ValidAcceptHeaderKt")
    }
}