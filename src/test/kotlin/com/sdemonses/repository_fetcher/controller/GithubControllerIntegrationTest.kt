package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.AbstractApplicationTest
import com.sdemonses.repository_fetcher.client.github.model.Owner
import com.sdemonses.repository_fetcher.client.github.model.Repository
import com.sdemonses.repository_fetcher.controller.model.RepositoriesResponse
import org.junit.jupiter.api.Test


class GithubControllerIntegrationTest : AbstractApplicationTest() {

    @Test
    fun `should return list of repositories belongs to user`() {
        val username = "someName"
        val repositories = listOf(Repository(false, "repositoryName", Owner(username)))
        stubGetGithubRepositories(username, repositories)
        repositories.forEach {
            stubGetRepositoryBranches(it.owner.login, it.name, emptyList())
        }


        restTemplate.getForObject("/v1/github/$username/repositories", RepositoriesResponse::class.java)
    }


}