package com.wook.kotlinbbs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class KotlinBbsApplication

fun main(args: Array<String>) {
    runApplication<KotlinBbsApplication>(*args)
}
