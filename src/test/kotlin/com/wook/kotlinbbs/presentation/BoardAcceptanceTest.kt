package com.wook.kotlinbbs.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wook.kotlinbbs.presentation.dto.BoardCreateRequest
import com.wook.kotlinbbs.presentation.dto.BoardResponse
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DisplayName("게시판 API 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class BoardAcceptanceTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @DisplayName("게시물 등록")
    @Test
    fun addBoard() {
        //given
        //게시물 등록 요청
        val boardCreateRequest = BoardCreateRequest("김태욱", "제목 테스트", "내용 테스트")

        //when
        //게시물 등록
        val resultActionDsl = `게시물 등록`(boardCreateRequest, mockMvc)

        //then
        //게시물 등록됨
        `게시물 등록됨`(boardCreateRequest, resultActionDsl)
    }

    @DisplayName("게시물 목록 조회")
    @Test
    fun getBoards() {
        //given
        //게시물 등록 되어 있음
        for (index in 1..10) {
            `게시물 등록 및 검증`(BoardCreateRequest("김태욱 $index", "제목 $index", "내용 $index"), mockMvc)
        }

        //when
        //게시물 목록 조회
        val resultActionDsl = `게시물 목록 조회`(mockMvc)

        //then
        //게시물 목록 조회됨
        `게시물 목록 조회됨`(resultActionDsl)
    }

    @DisplayName("게시물 조회")
    @Test
    fun getBoard() {
        //given
        //게시물 등록 되어 있음
        val boardResponse = `게시물 등록 및 검증`(BoardCreateRequest("김태욱", "제목", "내용"), mockMvc)

        //when
        //게시물 조회
        val resultActionDsl = `게시물 조회`(boardResponse.id, mockMvc)

        //then
        //게시물 조회됨
        `게시물 조회됨`(boardResponse, resultActionDsl)
    }

    @DisplayName("게시물 수정")
    @Test
    fun updateBoard() {
        //given
        //게시물 등록되어 있음
        val boardResponse = `게시물 등록 및 검증`(BoardCreateRequest("김태욱", "제목", "내용"), mockMvc)
        val boardRequest = BoardCreateRequest("김태욱", "제목 수정", "내용 수정")

        //when
        //게시물 수정
        val resultActionsDsl = `게시물 수정`(boardResponse, boardRequest, mockMvc)

        //then
        //게시물 수정됨
        `게시물 수정됨`(boardResponse, boardRequest, resultActionsDsl)
    }

    @Test
    fun deleteBoard() {
        //given
        //게시물 등록되어 있음
        val boardResponse = `게시물 등록 및 검증`(BoardCreateRequest("김태욱", "제목", "내용"), mockMvc)
        val boardRequest = BoardCreateRequest("김태욱", "제목 수정", "내용 수정")

        //when
        //게시물 삭제
        `게시물 삭제`(boardResponse, mockMvc)

        //then
        //게시물 삭제됨
        `게시물 삭제됨`(boardResponse.id, mockMvc)
    }

    companion object {
        fun `게시물 등록`(boardCreateRequest: BoardCreateRequest, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc
                .post("/boards") {
                    contentType = MediaType.APPLICATION_JSON_UTF8
                    content = jacksonObjectMapper().writeValueAsString(boardCreateRequest)
                }
                .andExpect {
                    status().isCreated
                    header { exists("location") }
                }
        }

        fun `게시물 등록됨`(boardCreateRequest: BoardCreateRequest, resultActionDsl: ResultActionsDsl) {
            resultActionDsl.andExpect {
                jsonPath("$.id").exists()
                jsonPath("$.author", equals(boardCreateRequest.author))
                jsonPath("$.title", equals(boardCreateRequest.title))
                jsonPath("$.content", equals(boardCreateRequest.content))
                jsonPath("$.createAt").exists()
                jsonPath("$.updateAt").exists()
            }
        }

        fun `게시물 등록 및 검증`(boardCreateRequest: BoardCreateRequest, mockMvc: MockMvc): BoardResponse {
            //when
            val resultActionsDsl = `게시물 등록`(boardCreateRequest, mockMvc)

            //then
            `게시물 등록됨`(boardCreateRequest, resultActionsDsl)

            return jacksonObjectMapper().readValue(resultActionsDsl.andReturn().response.contentAsString)
        }

        fun `게시물 목록 조회`(mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.get("/boards").andExpect { status().isOk }
        }

        fun `게시물 목록 조회됨`(resultActionDsl: ResultActionsDsl) {
            resultActionDsl.andExpect {
                jsonPath("$.boardResponses.*.id").exists()
                jsonPath("$.boardResponses.*.author").exists()
                jsonPath("$.boardResponses.*.title").exists()
                jsonPath("$.boardResponses.*.content").exists()
                jsonPath("$.boardResponses.*.createAt").exists()
                jsonPath("$.boardResponses.*.updateAt").exists()
            }
        }

        fun `게시물 조회`(id: Long, mockMvc: MockMvc): ResultActionsDsl {
            return mockMvc.get("/boards/{id}", id).andExpect { status().isOk }
        }

        fun `게시물 조회됨`(boardResponse: BoardResponse, resultActionDsl: ResultActionsDsl) {
            resultActionDsl.andExpect {
                jsonPath("$.id", equals(boardResponse.id))
                jsonPath("$.author", equals(boardResponse.author))
                jsonPath("$.title", equals(boardResponse.title))
                jsonPath("$.content", equals(boardResponse.content))
                jsonPath("$.createAt", equals(boardResponse.createAt))
                jsonPath("$.updateAt", equals(boardResponse.updateAt))
            }
        }

        private fun `게시물 수정`(
            boardResponse: BoardResponse, boardRequest: BoardCreateRequest, mockMvc: MockMvc
        ): ResultActionsDsl {
            return mockMvc
                .put("/boards/{id}", boardResponse.id) {
                    contentType = MediaType.APPLICATION_JSON_UTF8
                    content = jacksonObjectMapper().writeValueAsString(boardRequest)
                }
                .andExpect { status().isOk }
        }

        fun `게시물 수정됨`(
            boardResponse: BoardResponse, boardRequest: BoardCreateRequest, resultActionsDsl: ResultActionsDsl
        ) {
            resultActionsDsl.andExpect {
                jsonPath("$.id", equals(boardResponse.id))
                jsonPath("$.author", equals(boardRequest.author))
                jsonPath("$.title", equals(boardRequest.title))
                jsonPath("$.content", equals(boardRequest.content))
                jsonPath("$.createAt", equals(boardResponse.createAt))
                jsonPath("$.updateAt", !equals(boardResponse.updateAt))
            }
        }

        fun `게시물 삭제`(boardResponse: BoardResponse, mockMvc: MockMvc) {
            mockMvc.delete("/boards/{id}", boardResponse.id).andExpect { status().isNoContent }
        }

        fun `게시물 삭제됨`(id: Long, mockMvc: MockMvc) {
            assertThatThrownBy { `게시물 조회`(id, mockMvc) }.hasCauseInstanceOf(IllegalStateException::class.java)
        }
    }
}