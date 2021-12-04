package com.wook.kotlinbbs.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wook.kotlinbbs.presentation.BoardAcceptanceTest.Companion.`게시물 등록 및 검증`
import com.wook.kotlinbbs.presentation.dto.BoardCreateRequest
import com.wook.kotlinbbs.presentation.dto.BoardResponse
import com.wook.kotlinbbs.presentation.dto.CommentCreateRequest
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DisplayName("댓글 API 테스트")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CommentAcceptanceTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    private lateinit var boardResponse: BoardResponse

    @BeforeEach
    fun setUp() {
        //given
        //게시물이 등록되어 있음
        boardResponse = `게시물 등록 및 검증`(BoardCreateRequest("김태욱", "제목", "내용"), mockMvc)
    }

    @DisplayName("댓글 등록")
    @Test
    fun addComment() {
        //given
        //댓글 등록 요청
        val commentCreateRequest = CommentCreateRequest("김태욱", "댓글 내용입니다.")

        //when
        //댓글 등록
        val resultActionsDsl = `댓글 등록`(boardResponse.id, commentCreateRequest, mockMvc)

        //then
        //댓글 등록 됨
        `댓글 등록 됨`(commentCreateRequest, resultActionsDsl)
    }

    companion object {
        fun `댓글 등록`(id: Long, commentCreateRequest: CommentCreateRequest, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc
                .post("/boards/{id}/comments", id) {
                    contentType = MediaType.APPLICATION_JSON_UTF8
                    content = jacksonObjectMapper().writeValueAsString(commentCreateRequest)
                }
                .andExpect { status().isCreated }
        }

        fun `댓글 등록 됨`(commentCreateRequest: CommentCreateRequest, resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                jsonPath("$.id") { exists() }
                jsonPath("$.author") { `is`(commentCreateRequest.author) }
                jsonPath("$.content") { `is`(commentCreateRequest.content) }
            }
        }
    }
}