package io.barinek.continuum.allocations

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import java.util.*


@SpringBootApplication
@ComponentScan("io.barinek.continuum.allocations", "io.barinek.continuum.restsupport")
open class App

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    System.setProperty("server.port", System.getenv("PORT"))
    SpringApplication.run(App::class.java, *args)
}