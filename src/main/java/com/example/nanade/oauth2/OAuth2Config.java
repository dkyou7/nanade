package com.example.nanade.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class OAuth2Config {

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(OAuth2AuthClientService myOAuth2AuthorizedClientService) {
        return myOAuth2AuthorizedClientService;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        final ClientRegistration clientRegistration = CustomOAuthProvider.KAKAO.getBuilder().build();
        return new InMemoryClientRegistrationRepository(Collections.singletonList(clientRegistration));
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
