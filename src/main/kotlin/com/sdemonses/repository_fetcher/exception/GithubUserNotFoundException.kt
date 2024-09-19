package com.sdemonses.repository_fetcher.exception

class GithubUserNotFoundException(username:String): RuntimeException("User $username not found")