package com.example.clientserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class DemoController {
    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/messages")
    public String getMessages(
            @RegisteredOAuth2AuthorizedClient("student-client-authorization-code") OAuth2AuthorizedClient authorizedClient
    ) {
        return this.webClient.get()
                .uri("http://resource-server:8090/messages")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

//    @GetMapping(value = "/messages")
//    public Flux<String> getTweetsNonBlocking() {
//        Flux<String> tweetFlux = WebClient.create()
//                .get()
//                .uri("http://resource-server:8090/messages")
//                .retrieve()
//                .bodyToFlux(String.class);
//
//        tweetFlux.subscribe(t -> System.out.println(t));
//        return tweetFlux;
//    }

}
