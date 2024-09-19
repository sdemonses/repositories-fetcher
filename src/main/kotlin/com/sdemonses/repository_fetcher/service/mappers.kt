package com.sdemonses.repository_fetcher.service

import com.sdemonses.repository_fetcher.client.github.model.Branch
import com.sdemonses.repository_fetcher.client.github.model.Repository
import com.sdemonses.repository_fetcher.controller.model.BranchResponse
import com.sdemonses.repository_fetcher.controller.model.RepositoryResponse

fun mapRepositoryResponse(branches: List<Branch>, repository: Repository) =
    RepositoryResponse(
        repository.name,
        repository.owner.login,
        branches.map { branch -> branch.toBranchResponse() })

fun Branch.toBranchResponse() =
    BranchResponse(name, commit.sha)