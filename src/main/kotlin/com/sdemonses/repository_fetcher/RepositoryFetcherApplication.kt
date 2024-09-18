package com.sdemonses.repository_fetcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class RepositoryFetcherApplication

fun main(args: Array<String>) {
    runApplication<RepositoryFetcherApplication>(*args)
}
