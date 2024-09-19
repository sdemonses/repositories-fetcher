package com.sdemonses.repository_fetcher

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.sdemonses.repository_fetcher.client.github.model.Branch
import com.sdemonses.repository_fetcher.client.github.model.Repository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    classes = [RepositoryFetcherApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class AbstractApplicationTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @BeforeEach
    fun beforeEach() {
        wireMockServer.resetAll()
    }

    fun stubGetGithubRepositories(username: String, response: List<Repository> = emptyList(), status: Int = 200) {
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathEqualTo("/git/users/$username/repos"))
                .willReturn(
                    when (status) {
                        404 -> aResponse()
                            .withStatus(404)
                        else -> aResponse()
                            .withStatus(200)
                            .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .withBody(objectMapper.writeValueAsString(response))
                    }
                )
        )
    }

    fun stubGetRepositoryBranches(owner: String, repositoryName: String, response: List<Branch>) {
        wireMockServer.stubFor(
            WireMock
                .get(WireMock.urlPathEqualTo("/git/repos/$owner/$repositoryName/branches"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .withBody(objectMapper.writeValueAsString(response))
                )
        )
    }

    companion object {
        val wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().bindAddress("localhost").port(8125))

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            wireMockServer.start()
        }
    }

}
