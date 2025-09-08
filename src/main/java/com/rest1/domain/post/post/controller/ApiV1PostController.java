package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.post.dto.PostDto;
import com.rest1.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {

    private final PostService postService;

    @GetMapping
    @Transactional(readOnly = true)
    public List<PostDto> list() {
        return postService.findAll().stream()
                .map(post -> new PostDto(post))
                .toList();
    }

}
