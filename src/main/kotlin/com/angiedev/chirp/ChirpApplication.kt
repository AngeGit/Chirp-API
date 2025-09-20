package com.angiedev.chirp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class ChirpApplication

fun main(args: Array<String>) {
	runApplication<ChirpApplication>(*args)
}
