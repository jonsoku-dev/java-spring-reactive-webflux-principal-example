package com.example.webfluxexample;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class MyFilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> addFilter() {
        System.out.println("필터 등록됨 !!!");
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter());
        bean.addUrlPatterns("/sse");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> addFilter2() {
        System.out.println("필터2 등록됨 !!!");
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/add");
        return bean;
    }
}
