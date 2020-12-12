package com.example.nanade.account;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id @GeneratedValue
    private Long id;
    private String oauthid;
    private String name;
    private String providerName;
    private String accessToken;
    private String message;

    public Account(String id, String name, String registrationId, String tokenValue) {
        this.oauthid = id;
        this.name = name;
        this.providerName = registrationId;
        this.accessToken = tokenValue;
        this.message = "메세지 기본";
    }
}
