package com.example.nanade.login;

import com.example.nanade.account.Account;
import com.example.nanade.account.AccountRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;


@Controller
public class LoginController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/myplace")
    public String getMyAuthenticationFromSession(@AuthenticationPrincipal OAuth2User oAuth2User,Model model) {
        model.addAttribute("user",oAuth2User.getAttributes().get("kakao_account"));
        Account byOauthId = accountRepository.findByOauthid(oAuth2User.getName());
        model.addAttribute("account",byOauthId);
        return "place/myplace";
    }

    @PostMapping("send")
    public String sendKakao(@AuthenticationPrincipal OAuth2User oAuth2User) throws ParseException {
        Account byOauthId = accountRepository.findByOauthid(oAuth2User.getName());
        String url = "https://kapi.kakao.com/v2/api/talk/memo/scrap/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + byOauthId.getAccessToken());


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("request_url","http://localhost:8080");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/";
        }
        return null;

    }
}
