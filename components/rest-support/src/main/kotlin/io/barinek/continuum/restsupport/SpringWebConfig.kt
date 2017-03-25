package io.barinek.continuum.restsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
@EnableWebMvc
open class SpringWebConfig : WebMvcConfigurerAdapter() {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(MappingJackson2HttpMessageConverter(ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())!!))
        converters.add(StringHttpMessageConverter())
    }

    /// USED BY PROJECT CLIENT

    @Bean open fun getObjectMapper() = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())!!

    @Bean open fun getRestTemplate() = RestTemplate()
}