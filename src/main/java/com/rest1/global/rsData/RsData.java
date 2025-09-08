package com.rest1.global.rsData;

import com.rest1.domain.post.comment.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RsData {

    private String resultCode;
    private String msg;
    private CommentDto data;

}
