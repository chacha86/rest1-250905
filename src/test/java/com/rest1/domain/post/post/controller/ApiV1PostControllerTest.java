package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test") // 테스트 환경에서는 test 프로파일을 활성화합니다.
@SpringBootTest // 스프링부트 테스트 클래스임을 나타냅니다.
@AutoConfigureMockMvc // MockMvc를 자동으로 설정합니다.
@Transactional
public class ApiV1PostControllerTest {

    @Autowired
    private MockMvc mvc; // MockMvc를 주입받습니다.

    @Autowired
    private PostRepository postRepository;

    // 회원가입 테스트
    @Test
    @DisplayName("글 쓰기")
    void t1() throws Exception {
        // 회원가입 요청을 보냅니다.
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "제목",
                                            "content": "내용"
                                        }
                                        """)
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("write"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("4번 게시물이 생성되었습니다."))
                .andExpect(jsonPath("$.data.id").value(4))
                .andExpect(jsonPath("$.data.subject").value("제목"))
                .andExpect(jsonPath("$.data.body").value("내용"));
    }

    @Test
    @DisplayName("글 수정")
    void t2() throws Exception {

        int targetId = 1;
        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";

        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/posts/%d".formatted(targetId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "%s",
                                            "content": "%s"
                                        }
                                        """.formatted(newTitle, newContent))
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 글이 수정되었습니다.".formatted(targetId)));

        Post post = postRepository.findById((long) targetId).get();

        assertThat(post.getTitle()).isEqualTo(newTitle);
        assertThat(post.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("글 삭제")
    void t3() throws Exception {

        int targetId = 1;

        ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/posts/%d".formatted(targetId))
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("deleteItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 글이 삭제되었습니다.".formatted(targetId)));

    }

    @Test
    @DisplayName("글 단건 조회")
    void t4() throws Exception {

        int targetId = 1;

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts/%d".formatted(targetId))
                ).andDo(print());

        Post post = postRepository.findById((long) targetId).get();

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.subject").value(post.getTitle()))
                .andExpect(jsonPath("$.body").value(post.getContent()))
                .andExpect(jsonPath("$.createDate").value(post.getCreateDate().toString()))
                .andExpect(jsonPath("$.modifyDate").value(post.getModifyDate().toString()));
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.subject").isString())
//                .andExpect(jsonPath("$.body").isString())
//                .andExpect(jsonPath("$.createDate").isString())
//                .andExpect(jsonPath("$.modifyDate").isString());




    }

    @Test
    @DisplayName("글 다건 조회")
    void t5() throws Exception {

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/posts")
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getItems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[*].id", containsInRelativeOrder(3, 1)));



    }
}
