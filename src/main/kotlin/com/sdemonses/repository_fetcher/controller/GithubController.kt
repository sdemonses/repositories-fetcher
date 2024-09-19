package com.sdemonses.repository_fetcher.controller

import com.sdemonses.repository_fetcher.controller.model.RepositoriesResponse
import com.sdemonses.repository_fetcher.service.GithubService
import com.sdemonses.repository_fetcher.util.logger
import com.sdemonses.repository_fetcher.validation.validateAcceptHeader
import jakarta.validation.constraints.Min
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/github/users")
@Validated
class GithubController(
    private val githubService: GithubService
) {

    @GetMapping("/{username}/repositories")
    @ResponseStatus(HttpStatus.OK)
    fun getNotForkRepositories(
        @RequestHeader(name = HttpHeaders.ACCEPT, required = true) acceptHeader: String,
        @PathVariable username: String,
        @Min(1) @RequestParam(required = false) page: Int = 1,
        @Min(1) @RequestParam(required = false) size: Int = 30,
        @RequestParam(required = false) includeForks: Boolean = false
    ): RepositoriesResponse {
        log.info("'v1/github/users/$username/repositories' request received")
        validateAcceptHeader(acceptHeader)
        return githubService.getAllNonForkRepositories(username, page, size, includeForks)
    }

    companion object {
        private val log = logger()
    }
}