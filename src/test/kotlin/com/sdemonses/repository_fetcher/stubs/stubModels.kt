package com.sdemonses.repository_fetcher.stubs

import com.sdemonses.repository_fetcher.client.github.model.Branch
import com.sdemonses.repository_fetcher.client.github.model.Commit
import com.sdemonses.repository_fetcher.client.github.model.Owner
import com.sdemonses.repository_fetcher.client.github.model.Repository
import kotlin.random.Random

fun stubRepository(
    fork: Boolean = false,
    name: String = randomString(15),
    owner: String = randomString(5)
) = Repository(
    fork,
    name,
    Owner(owner)
)

fun stubBranch(
    name: String = randomString(10),
    sha: String = randomString(30)
) = Branch(
    name,
    Commit(sha)
)

val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun randomString(length: Int) = (1..length)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")