package com.wook.kotlinbbs.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wook.kotlinbbs.presentation.BoardAcceptanceTest.Companion.`게시물 등록 및 검증`
import com.wook.kotlinbbs.presentation.dto.BoardCreateRequest
import com.wook.kotlinbbs.presentation.dto.BoardResponse
import com.wook.kotlinbbs.presentation.dto.CommentCreateRequest
import com.wook.kotlinbbs.presentation.dto.CommentResponse
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DisplayName("댓글 API 테스트")
@AutoConfigureMockMvc
@SpringBootTest
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

    @DisplayName("댓글 목록 조회")
    @Test
    fun getComments() {
        //given
        //댓글 등록 되어있음
        for (count in 1..10) {
            `댓글 등록 및 검증`(boardResponse.id, CommentCreateRequest("김태욱 $count", "${count}번째 댓글 내용입니다."), mockMvc)
        }

        //when
        //댓글 목록 조회
        val resultActionsDsl = `댓글 목록 조회`(boardResponse.id, mockMvc)

        //then
        //댓글 목록 조회됨
        `댓글 목록 조회됨`(resultActionsDsl)
    }

    companion object {
        fun `댓글 등록`(id: Long, commentCreateRequest: CommentCreateRequest, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc
                .post("/boards/{id}/comments", id) {
                    contentType = MediaType.APPLICATION_JSON_UTF8
                    content = jacksonObjectMapper().writeValueAsString(commentCreateRequest)
                }
        }

        fun `댓글 등록 및 검증`(id: Long, commentCreateRequest: CommentCreateRequest, mockMvc: MockMvc): CommentResponse {
            val resultActionsDsl = `댓글 등록`(id, commentCreateRequest, mockMvc)

            `댓글 등록 됨`(commentCreateRequest, resultActionsDsl)

            return jacksonObjectMapper().readValue(resultActionsDsl.andReturn().response.contentAsString)
        }

        fun `댓글 등록 됨`(commentCreateRequest: CommentCreateRequest, resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                status().isCreated
                header { exists("location") }
                jsonPath("$.id") { exists() }
                jsonPath("$.author") { `is`(commentCreateRequest.author) }
                jsonPath("$.content") { `is`(commentCreateRequest.content) }
            }
        }

        private fun `댓글 목록 조회`(id: Long, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.get("/boards/{id}/comments", id)
        }

        fun `댓글 목록 조회됨`(resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                status().isOk
                jsonPath("$.commentResponses.*.id") { exists() }
                jsonPath("$.commentResponses.*.author") { exists() }
                jsonPath("$.commentResponses.*.content") { exists() }
            }
        }
    }
}