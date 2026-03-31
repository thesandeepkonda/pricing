package com.spacezant.pricing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://rest.avatax.com") // ✅ PRODUCTION
                .defaultHeaders(headers ->
                        headers.setBasicAuth("2006550736", "0E5927115DDF9B64")
                )
                .build();
    }
}
//public class WebClientConfig {
//
//    @Bean
//    public WebClient webClient() {
//        return WebClient.builder()
//                .baseUrl("https://api.taxcloud.com")
//                .build();
//    }
//}