package com.rest1.domain.post.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String title;
    private String content;

    public PostDto(Long id, LocalDateTime createDate, LocalDateTime modifyDate, String title, String content) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.title = title;
        this.content = content;
    }
}
