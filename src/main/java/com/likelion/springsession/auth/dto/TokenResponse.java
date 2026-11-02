package com.likelion.springsession.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// auth/dto/TokenResponse.java
@Getter
@RequiredArgsConstructor
public class TokenResponse {

    private final String accessToken;
    private final String refreshToken;
}
