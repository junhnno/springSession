package com.likelion.springsession.post.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//3주차
@Getter
@RequiredArgsConstructor
public class PostDetailResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
}
