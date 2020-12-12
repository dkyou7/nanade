package com.example.nanade.login;

import com.example.nanade.account.Account;
import com.example.nanade.account.AccountRepository;
import com.example.nanade.account.MessageForm;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.json.simple.JSONArray;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;


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
        model.addAttribute("messageForm",new MessageForm());
        return "place/myplace";
    }

    @GetMapping("success")
    public String sendKakao(@AuthenticationPrincipal OAuth2User oAuth2User) throws ParseException {
        return "place/success";
    }
    @PostMapping("send")
    public String sendKakaoText(@AuthenticationPrincipal OAuth2User oAuth2User,@Valid MessageForm messageForm, Errors errors) throws ParseException {
        Account byOauthId = accountRepository.findByOauthid(oAuth2User.getName());
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + byOauthId.getAccessToken());

        MultiValueMap<String, JSONObject> params = new LinkedMultiValueMap<>();
        try {
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            JSONObject sObject2 = new JSONObject();//배열 내에 들어갈 json
            sObject.put("object_type","text");
            sObject.put("text",messageForm.getMessage());
            sObject2.put("web_url", "http://localhost:8080/success");
            sObject2.put("mobile_web_url", "http://localhost:8080/success");
            sObject.put("link",sObject2);
//            sObject.put("button_title","바로 확인");
            params.add("template_object",sObject);

            HttpEntity<MultiValueMap<String, JSONObject>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return "redirect:/";
            }
            return null;
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
