package com.thy.factory;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

@Component
public class WebClientFactory {
    public WebClient createClient(String baseUrl, Map<String, String> headers) {

        WebClient.Builder builder =
                WebClient.builder()
                        .baseUrl(baseUrl)
                        .clientConnector(
                                new ReactorClientHttpConnector(
                                        HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)));

        headers.forEach(builder::defaultHeader);

        return builder.build();
    }
}
