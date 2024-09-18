package com.sdemonses.repository_fetcher.client.github

import com.sdemonses.repository_fetcher.client.github.model.Branch
import com.sdemonses.repository_fetcher.client.github.model.Repository
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "githubApi",
    url = "\${client.github.url}",
)
interface GithubClient {
    @GetMapping("/users/{username}/repos")
    fun getRepositories(@PathVariable username: String): List<Repository>

    @GetMapping("/repos/{owner}/{repository}/branches")
    fun getBranches(@PathVariable owner: String, @PathVariable repository: String): List<Branch>
}