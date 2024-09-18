package com.sdemonses.repository_fetcher.service

import com.sdemonses.repository_fetcher.client.github.GithubClient
import com.sdemonses.repository_fetcher.controller.model.BranchResponse
import com.sdemonses.repository_fetcher.controller.model.RepositoriesResponse
import com.sdemonses.repository_fetcher.controller.model.RepositoryResponse
import org.springframework.stereotype.Service

@Service
class GithubService(
    private val githubClient: GithubClient
) {

    fun getAllNonForkRepositories(username: String) =
        githubClient.getRepositories(username)
            .filter { !it.fork }
            .associateBy { githubClient.getBranches(it.owner.login, it.name) }
            .map {
                RepositoryResponse(
                    it.value.name,
                    it.value.owner.login,
                    it.key.map { branch -> BranchResponse(branch.name, branch.commit.sha) })
            }
            .let { RepositoriesResponse(it) }
}
