package io.barinek.continuum.allocations

import io.barinek.continuum.restsupport.SpringApp
import java.util.*

class App() : SpringApp(System.getenv("PORT").toInt(), "io.barinek.continuum")

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    App().start()
}