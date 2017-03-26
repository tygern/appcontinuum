package io.barinek.continuum.restsupport

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

@Configuration
open class RestTemplate {
    @Bean open fun getRestTemplate() = RestTemplate()
}

fun RestTemplate.post(url: String, data: String): String {
    val headers = HttpHeaders()
    headers.set("Content-type", "application/json")
    val entity = HttpEntity<String>(data, headers)
    val response = exchange(url, HttpMethod.POST, entity, String::class.java)
    return response.body ?: ""
}

fun RestTemplate.get(url: String, vararg params: Pair<String, String>): String {
    val headers = HttpHeaders()
    headers.set("Content-type", "application/json")
    val entity = HttpEntity<String>(headers)
    val response = exchange(url, HttpMethod.GET, entity, String::class.java, mapOf(*params))
    return response.body ?: ""
}