package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.post.dto.PostDto;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.service.PostService;
import com.rest1.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {

    private final PostService postService;

    @GetMapping
    @Transactional(readOnly = true)
    public List<PostDto> getItems() {
        return postService.findAllLatest().stream()
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

    @DeleteMapping("/{id}")
    public RsData<Void> deleteItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        postService.delete(post);

        return new RsData<Void>(
                "200-1",
                "%d번 글이 삭제되었습니다.".formatted(id)
        );
    }

    record PostWriteReqBody(
            @NotBlank
            @Size(min = 2, max = 10)
            String title,
            @NotBlank
            @Size(min = 2, max = 100)
            String content
    ) {
    }

    @PostMapping
    public RsData<PostDto> write(
            @RequestBody @Valid PostWriteReqBody reqBody
    ) {

        Post post = postService.write(reqBody.title, reqBody.content);
        return new RsData<>(
                "201-1",
                "%d번 글이 생성되었습니다.".formatted(post.getId()),
                new PostDto(post)
        );
    }

    record PostModifyReqBody(
            String title,
            String content
    ) {}

    @PutMapping("/{id}")
    @Transactional
    public RsData<Void> modify(
            @PathVariable Long id,
            @RequestBody PostModifyReqBody reqBody
    ) {
        Post post = postService.findById(id).get();
        postService.modify(post, reqBody.title, reqBody.content);

        return new RsData<>(
                "200-1",
                "%d번 글이 수정되었습니다.".formatted(id)
        );
    }

}
