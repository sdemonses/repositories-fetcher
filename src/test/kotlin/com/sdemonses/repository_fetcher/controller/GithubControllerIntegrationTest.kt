package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.AbstractApplicationTest
import com.sdemonses.repository_fetcher.controller.model.ErrorListResponse
import com.sdemonses.repository_fetcher.controller.model.RepositoriesResponse
import com.sdemonses.repository_fetcher.controller.model.SimpleErrorResponse
import com.sdemonses.repository_fetcher.exception.AcceptHeaderNotValidException.Companion.ACCEPT_HEADER_ERROR_MESSAGE
import com.sdemonses.repository_fetcher.stubs.stubBranch
import com.sdemonses.repository_fetcher.stubs.stubRepository
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import kotlin.test.assertEquals

class GithubControllerIntegrationTest : AbstractApplicationTest() {

    @Test
    fun `should return list of non fork repositories belongs to user`() {
        val username = "someName"
        val repository = stubRepository()
        val repository1 = stubRepository(true)
        val repositories = listOf(repository, repository1)
        stubGetGithubRepositories(username, repositories)
        val branch = stubBranch()
        val branch1 = stubBranch()
        stubGetRepositoryBranches(repository.owner.login, repository.name, listOf(branch, branch1))
        val headers = HttpHeaders()
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        val entity = HttpEntity<Void>(headers)
        val response = restTemplate.exchange(
            "/v1/github/users/$username/repositories",
            HttpMethod.GET,
            entity,
            RepositoriesResponse::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body!!.repositories.size)
        val actualRepository = response.body!!.repositories.first()
        assertEquals(repository.name, actualRepository.name)
        assertEquals(repository.owner.login, actualRepository.ownerLogin)

        assertEquals(2, actualRepository.branches.size)
        assertEquals(branch.commit.sha, actualRepository.branches.firstOrNull { it.name == branch.name }?.lastCommit)
        assertEquals(branch1.commit.sha, actualRepository.branches.firstOrNull { it.name == branch1.name }?.lastCommit)
    }

    @Test
    fun `should fail if Accept header not passed`() {
        val headers = HttpHeaders()
        val entity = HttpEntity<Void>(headers)

        val response = restTemplate.exchange(
            "/v1/github/users/someName/repositories", HttpMethod.GET, entity, SimpleErrorResponse::class.java
        )

        assertEquals(406, response.statusCode.value())

        assertEquals(ACCEPT_HEADER_ERROR_MESSAGE, response.body!!.message)
        assertEquals(406, response.body!!.status)
    }

    @Test
    fun `should fail if user not found`() {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        val entity = HttpEntity<Void>(headers)
        val username = "something"
        stubGetGithubRepositories(username, status = 404)

        val response = restTemplate.exchange(
            "/v1/github/users/$username/repositories", HttpMethod.GET, entity, SimpleErrorResponse::class.java
        )

        assertEquals(404, response.statusCode.value())

        assertEquals("User $username not found", response.body!!.message)
        assertEquals(404, response.body!!.status)
    }

    @Test
    fun `should fail if size and page lower than allowed`() {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        val entity = HttpEntity<Void>(headers)
        val username = "something"
        stubGetGithubRepositories(username, status = 404)

        val response = restTemplate.exchange(
            "/v1/github/users/$username/repositories?page={page}&size={size}",
            HttpMethod.GET,
            entity,
            ErrorListResponse::class.java,
            mapOf("page" to 0, "size" to 0)
        )

        assertEquals(400, response.statusCode.value())
        assertEquals(2, response.body!!.messages.filter { it == "must be greater than or equal to 1" }.size)

    }
}