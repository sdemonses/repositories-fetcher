package com.sdemonses.repository_fetcher.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger {
    val javaClass = T::class.java
    val declaringClass = javaClass.declaringClass
    return if (declaringClass == null) {
        LoggerFactory.getLogger(javaClass)
    } else LoggerFactory.getLogger(declaringClass)

}