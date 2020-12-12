package com.example.nanade.oauth2;

import com.example.nanade.account.Account;
import com.example.nanade.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class OAuth2AuthClientService implements OAuth2AuthorizedClientService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient oAuth2AuthorizedClient, Authentication authentication) {
        String registrationId = oAuth2AuthorizedClient.getClientRegistration().getRegistrationId();
        OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String id = String.valueOf(oAuth2User.getAttributes().get("id"));
        String name = (String) ((LinkedHashMap) ((LinkedHashMap) oAuth2User.getAttribute("kakao_account")).get("profile")).get("nickname");
        Account member = new Account(id, name, registrationId, accessToken.getTokenValue());
        accountRepository.save(member);
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String s, String s1) {
        return null;
    }
    @Override
    public void removeAuthorizedClient(String s, String s1) {
        return;
    }
}
