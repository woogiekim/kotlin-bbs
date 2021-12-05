package com.wook.kotlinbbs.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wook.kotlinbbs.presentation.BoardAcceptanceTest.Companion.`게시물 등록 및 검증`
import com.wook.kotlinbbs.presentation.dto.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
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

    @DisplayName("댓글 수정")
    @Test
    internal fun updateComment() {
        //given
        //댓글 등록 되어있음
        val commentResponse = `댓글 등록 및 검증`(boardResponse.id, CommentCreateRequest("김태욱", "댓글 내용입니다."), mockMvc)

        //댓글 수정 요청
        val commentUpdateRequest = CommentUpdateRequest("댓글 내용 수정입니다.")

        //when
        //댓글 수정
        val resultActionsDsl = `댓글 수정`(boardResponse.id, commentResponse.id, commentUpdateRequest, mockMvc)

        //then
        //댓글 수정됨
        `댓글 수정됨`(commentUpdateRequest, resultActionsDsl)
    }

    @DisplayName("댓글 삭제")
    @Test
    fun deleteComment() {
        //given
        //댓글 등록 되어있음
        val commentResponse = `댓글 등록 및 검증`(boardResponse.id, CommentCreateRequest("김태욱", "댓글 내용입니다."), mockMvc)

        //댓글 삭제
        val resultActionsDsl = `댓글 삭제`(boardResponse.id, commentResponse.id, mockMvc)

        //댓글 삭제됨
        `댓글 삭제됨`(boardResponse.id, commentResponse.id, resultActionsDsl, mockMvc)
    }

    companion object {
        fun `댓글 등록`(boardId: Long, commentCreateRequest: CommentCreateRequest, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc
                .post("/boards/{boardId}/comments", boardId) {
                    contentType = MediaType.APPLICATION_JSON_UTF8
                    content = jacksonObjectMapper().writeValueAsString(commentCreateRequest)
                }
        }

        fun `댓글 등록 및 검증`(boardId: Long, commentCreateRequest: CommentCreateRequest, mockMvc: MockMvc): CommentResponse {
            val resultActionsDsl = `댓글 등록`(boardId, commentCreateRequest, mockMvc)

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

        fun `댓글 목록 조회`(boardId: Long, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.get("/boards/{boardId}/comments", boardId) { accept = MediaType.APPLICATION_JSON_UTF8 }
        }

        fun `댓글 목록 조회됨`(resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                status().isOk
                jsonPath("$.commentResponses.*.id") { exists() }
                jsonPath("$.commentResponses.*.author") { exists() }
                jsonPath("$.commentResponses.*.content") { exists() }
            }
        }

        fun `댓글 수정`(
            boardId: Long,
            id: Long,
            commentUpdateRequest: CommentUpdateRequest,
            mockMvc: MockMvc
        ): ResultActionsDsl {
            return mockMvc.put("/boards/{boardId}/comments/{id}", boardId, id) {
                contentType = MediaType.APPLICATION_JSON_UTF8
                content = commentUpdateRequest
            }
        }

        fun `댓글 수정됨`(commentUpdateRequest: CommentUpdateRequest, resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                status().isOk
                jsonPath("$.content") { `is`(commentUpdateRequest.content) }
            }
        }

        fun `댓글 삭제`(boardId: Long, id: Long, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.delete("/boards/{boardId}/comments/{id}", boardId, id)
        }

        fun `댓글 삭제됨`(boardId: Long, id: Long, resultActionsDsl: ResultActionsDsl, mockMvc: MockMvc) {
            resultActionsDsl.andExpect { status().isNoContent }
            val commentResponses = jacksonObjectMapper().readValue(
                `댓글 목록 조회`(boardId, mockMvc)
                    .andExpect { status().isOk }
                    .andReturn().response.contentAsString,
                CommentResponses::class.java
            )
            assertThat(commentResponses.commentResponses)
                .extracting(CommentResponse::id)
                .doesNotContain(Tuple(id))
        }
    }
}