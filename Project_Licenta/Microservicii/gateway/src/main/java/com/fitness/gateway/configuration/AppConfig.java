package com.fitness.gateway.configuration;

import com.fitness.gateway.cors.CORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilter() {
        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CORSFilter());
        registrationBean.addUrlPatterns("/*"); // Aplică filtrul pentru toate URL-urile
        registrationBean.setName("CORSFilter");
        registrationBean.setOrder(1); // Setează ordinea în care filtrul va fi aplicat, dacă ai mai multe filtre

        return registrationBean;
    }
}

