package com.wook.kotlinbbs.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wook.kotlinbbs.presentation.BoardAcceptanceTest.Companion.`게시물 등록 및 검증`
import com.wook.kotlinbbs.presentation.dto.BoardCreateRequest
import com.wook.kotlinbbs.presentation.dto.BoardResponse
import com.wook.kotlinbbs.presentation.dto.LikeRequest
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
import org.springframework.test.web.servlet.post

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
        `좋아요 됨`(likeRequest, boardResponse, resultActionsDsl)
    }

    companion object {
        fun `좋아요`(boardId: Long, likeRequest: LikeRequest, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.post("/boards/{boardId}/likes", boardId) {
                contentType = MediaType.APPLICATION_JSON_UTF8
                accept = MediaType.APPLICATION_JSON_UTF8
                content = jacksonObjectMapper().writeValueAsString(likeRequest)
            }
        }

        fun `좋아요 됨`(likeRequest: LikeRequest, boardResponse: BoardResponse, resultActionsDsl: ResultActionsDsl) {
            resultActionsDsl.andExpect {
                status { isCreated() }
                header { exists("location") }
                jsonPath("$.author") { `is`(likeRequest.author) }
                jsonPath("$.id") { `is`(boardResponse.id) }
                jsonPath("$.author") { `is`(boardResponse.author) }
                jsonPath("$.title") { `is`(boardResponse.title) }
                jsonPath("$.content") { `is`(boardResponse.content) }
                jsonPath("$.createAt") { `is`(boardResponse.createAt) }
                jsonPath("$.updateAt") { `is`(boardResponse.updateAt) }
            }
        }
    }
}