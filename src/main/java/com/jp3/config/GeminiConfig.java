package com.jp3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    //restAPIのクライアント情報をBeanに詰めて使いまわす
    @Bean
    RestClient geminiRestClient() {
        return RestClient.builder()
                .baseUrl(apiUrl + "?key=" + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}