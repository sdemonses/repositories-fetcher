package com.sdemonses.repository_fetcher.controller.model

data class RepositoriesResponse(
    val repositories: List<RepositoryResponse>
)

data class RepositoryResponse(
    val name: String, val ownerLogin: String, val branches: List<BranchResponse>
)

data class BranchResponse(
    val name: String, val lastCommit: String
)