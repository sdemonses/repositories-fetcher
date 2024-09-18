package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.service.GithubService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/github")
class GithubController(
    private val githubService: GithubService
) {

    @GetMapping("/{username}/repositories")
    @ResponseStatus(HttpStatus.OK)
    fun getNotForkRepositories(
        @RequestHeader(name = HttpHeaders.ACCEPT) acceptHeader: String,
        @PathVariable username: String
    ) =
        githubService.getAllNonForkRepositories(username)

}