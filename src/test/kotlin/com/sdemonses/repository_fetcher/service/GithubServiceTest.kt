package com.sdemonses.repository_fetcher.service

import com.sdemonses.repository_fetcher.client.github.GithubClient
import com.sdemonses.repository_fetcher.exception.GithubUserNotFoundException
import com.sdemonses.repository_fetcher.stubs.stubBranch
import com.sdemonses.repository_fetcher.stubs.stubRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GithubServiceTest {

    private val githubClient: GithubClient = mockk()
    private val repositoryService = GithubService(githubClient) // Assuming this method is in RepositoryService

    @Test
    fun `should return non-forked repositories`() {
        val username = "testUser"
        val page = 1
        val size = 5
        val includeForks = false

        val repo1 = stubRepository()
        val repo2 = stubRepository(fork = true)
        val branch1 = listOf(stubBranch())

        every { githubClient.getRepositories(username, page, size) } returns listOf(repo1, repo2)
        every { githubClient.getBranches(repo1.owner.login, repo1.name) } returns branch1

        val result = repositoryService.getAllNonForkRepositories(username, page, size, includeForks)

        assertNotNull(result)
        assertEquals(1, result.repositories.size)
        verify(exactly = 1) { githubClient.getRepositories(username, page, size) }
        verify(exactly = 1) { githubClient.getBranches(repo1.owner.login, repo1.name) }
    }

    @Test
    fun `should include forked repositories when includeForks is true`() {
        val username = "testUser"
        val page = 1
        val size = 5
        val includeForks = true

        val repo1 = stubRepository()
        val repo2 = stubRepository(fork = true)
        val branch1 = listOf(stubBranch())
        val branch2 = listOf(stubBranch())

        every { githubClient.getRepositories(username, page, size) } returns listOf(repo1, repo2)
        every { githubClient.getBranches(repo1.owner.login, repo1.name) } returns branch1
        every { githubClient.getBranches(repo2.owner.login, repo2.name) } returns branch2

        val result = repositoryService.getAllNonForkRepositories(username, page, size, includeForks)

        assertNotNull(result)
        assertEquals(2, result.repositories.size)
    }

    @Test
    fun `should throw GithubUserNotFoundException when no repositories are returned`() {
        // Arrange
        val username = "nonexistentUser"
        val page = 1
        val size = 5
        every { githubClient.getRepositories(username, page, size) } returns null

        // Act & Assert
        val exception = assertThrows<GithubUserNotFoundException> {
            repositoryService.getAllNonForkRepositories(username, page, size)
        }

        assertEquals("User: 'nonexistentUser' not found", exception.message)
        verify(exactly = 1) { githubClient.getRepositories(username, page, size) }
    }
}