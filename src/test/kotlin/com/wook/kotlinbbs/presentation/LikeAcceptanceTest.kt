package com.wook.kotlinbbs.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wook.kotlinbbs.presentation.BoardAcceptanceTest.Companion.`게시물 등록 및 검증`
import com.wook.kotlinbbs.presentation.dto.*
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import java.util.stream.IntStream
import kotlin.streams.toList

@DisplayName("좋아요 API 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class LikeAcceptanceTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    private lateinit var boardResponse: BoardResponse

    @BeforeEach
    fun setUp() {
        //given
        //게시물이 등록되어 있음
        boardResponse = `게시물 등록 및 검증`(BoardCreateRequest("김태욱", "제목", "내용"), mockMvc)
    }

    @Test
    fun like() {
        //given
        //좋아요 요청
        val likeRequest = LikeRequest("김태욱")

        //when
        //좋아요
        val resultActionsDsl = `좋아요`(boardResponse.id, likeRequest, mockMvc)

        //then
        //좋아요 됨
        `좋아요 됨`(likeRequest, resultActionsDsl)
    }

    @Test
    fun getLikes() {
        //given
        //좋아요 되어 있음
        val likeResponses = IntStream.range(1, 10)
            .mapToObj { `좋아요 및 검증`(boardResponse.id, LikeRequest("김태욱 $it"), mockMvc) }
            .toList()
            .run { LikeResponses(this) }

        //when
        //좋아요 목록 조회
        val resultActionsDsl = `좋아요 목록 조회`(boardResponse.id, mockMvc)

        //then
        //좋아요 목록 조회 됨
        `좋아요 목록 조회 됨`(likeResponses, resultActionsDsl)
    }

    @Test
    fun unlike() {
        //given
        //좋아요 되어 있음
        `좋아요 및 검증`(boardResponse.id, LikeRequest("김태욱"), mockMvc)
        val likeResponse = `좋아요 및 검증`(boardResponse.id, LikeRequest("김태욱"), mockMvc)

        //when
        //좋아요 취소
        val resultActionsDsl = `좋아요 취소`(boardResponse.id, likeResponse.id, mockMvc)

        //then
        //좋아요 취소 됨
        `좋아요 취소 됨`(likeResponse, resultActionsDsl)
    }

    private fun `좋아요 취소 됨`(likeResponse: LikeResponse, resultActionsDsl: ResultActionsDsl) {
        resultActionsDsl.andExpect {
            status { isNoContent() }
            jsonPath("$.likeResponses.*.id") { not(likeResponse.id) }
        }
    }

    companion object {
        fun `좋아요`(boardId: Long, likeRequest: LikeRequest, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.post("/boards/{boardId}/likes", boardId) {
                contentType = MediaType.APPLICATION_JSON_UTF8
                accept = MediaType.APPLICATION_JSON_UTF8
                content = jacksonObjectMapper().writeValueAsString(likeRequest)
            }
        }

        @Test
        fun `좋아요 및 검증`(boardId: Long, likeRequest: LikeRequest, mockMvc: MockMvc): LikeResponse {
            val resultActionsDsl = `좋아요`(boardId, likeRequest, mockMvc)

            `좋아요 됨`(likeRequest, resultActionsDsl)

            return jacksonObjectMapper().readValue(
                resultActionsDsl.andReturn().response.contentAsString,
                LikeResponse::class.java
            )
        }

        fun `좋아요 됨`(likeRequest: LikeRequest, resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                status { isCreated() }
                header { exists("location") }
                jsonPath("$.author") { `is`(likeRequest.author) }
            }
        }

        fun `좋아요 목록 조회`(boardId: Long, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.get("/boards/{boardId}/likes", boardId) {
                accept = MediaType.APPLICATION_JSON_UTF8
            }
        }

        fun `좋아요 목록 조회 됨`(likeResponses: LikeResponses, resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                status { isOk() }
                jsonPath("$.likeResponses") { contains(likeResponses.likeResponses) }
            }
        }

        fun `좋아요 취소`(boardId: Long, id: Long, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.delete("/boards/{boardId}/likes/{id}", boardId, id)
        }
    }
}