package com.sdemonses.repository_fetcher.client.github.model

data class Repository(
    val fork: Boolean,
    val name: String,
    val owner: Owner

)

data class Owner(
    val login: String
)

data class Branch(
    val name: String,
    val commit: Commit
)

data class Commit(
    val sha: String
)