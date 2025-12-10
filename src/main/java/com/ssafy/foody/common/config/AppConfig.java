package com.ssafy.foody.common.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;


@Configuration
public class AppConfig {
	@Bean
    public WebClient webClient() {
        // Netty HttpClient 설정 (타임아웃 등)
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) // 연결 타임아웃 (3초)
                .responseTimeout(Duration.ofSeconds(10));           // 응답 타임아웃 (10초)

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
