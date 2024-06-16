package com.jong.msa.board.client.post.enums;

import com.jong.msa.board.core.web.enums.ErrorCodeEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCodeEnum {

	NOT_FOUND_POST("POST-001", "존재하지 않는 게시글입니다."),
	NOT_FOUND_POST_WRITER("POST-002", "존재하지 않는 게시글 작성자입니다.");

	private final String code;
	
	private final String message;
	
}
