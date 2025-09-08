package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.post.dto.PostDto;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.service.PostService;
import com.rest1.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public List<PostDto> getItems() {
        return postService.findAll().stream()
                .map(PostDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostDto getItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        return new PostDto(post);
    }

    @GetMapping("/{id}/delete")
    public RsData deleteItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        postService.delete(post);

        return new RsData(
          "204-1",
          "%d번 게시물이 삭제되었습니다.".formatted(id),
          new PostDto(post)
        );
    }

}
