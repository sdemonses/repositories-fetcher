package com.sdemonses.repository_fetcher.service

import com.sdemonses.repository_fetcher.client.github.GithubClient
import com.sdemonses.repository_fetcher.controller.model.RepositoriesResponse
import com.sdemonses.repository_fetcher.exception.GithubUserNotFoundException
import org.springframework.stereotype.Service

@Service
class GithubService(
    private val githubClient: GithubClient
) {

    fun getAllNonForkRepositories(username: String, page: Int, size: Int, includeForks: Boolean = false) =
        (githubClient.getRepositories(username, page, size) ?: throw GithubUserNotFoundException(username))
            .let { reps -> if (includeForks) reps else reps.filterNot { it.fork } }
            .associateBy { githubClient.getBranches(it.owner.login, it.name) }
            .map { (branches, repository) ->
                mapRepositoryResponse(branches, repository)
            }
            .let { RepositoriesResponse(it) }
}
