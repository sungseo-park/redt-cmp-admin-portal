package com.cmp.adminportal.config

import io.github.jhipster.config.locale.AngularCookieLocaleResolver

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
class LocaleConfiguration : WebMvcConfigurer {

    @Bean(name = ["localeResolver"])
    fun localeResolver(): LocaleResolver {
        return AngularCookieLocaleResolver().apply { cookieName = "NG_TRANSLATE_LANG_KEY" }
    }

    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry!!.addInterceptor(LocaleChangeInterceptor().apply { paramName = "language" })
    }
}
