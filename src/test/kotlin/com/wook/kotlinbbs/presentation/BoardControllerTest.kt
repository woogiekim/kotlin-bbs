package com.wook.kotlinbbs.presentation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wook.kotlinbbs.presentation.dto.BoardRequest
import com.wook.kotlinbbs.presentation.dto.BoardResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@DisplayName("게시판 API 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
) {

    @DisplayName("게시물 등록")
    @Test
    fun addBoard() {
        //given
        val author = "김태욱"
        val title = "제목 테스트"
        val content = "내용 테스트"
        val boardRequest = BoardRequest(author, title, content)

        //when
        val resultActions = `게시물 등록`(boardRequest)

        //then
        `게시물 등록됨`(resultActions, author, title, content)
    }

    fun `게시물 등록`(boardRequest: BoardRequest): ResultActions {
        return mockMvc
            .perform(
                post("/boards").contentType(MediaType.APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(boardRequest))
            )
            .andExpect(status().isCreated).andExpect(header().exists("location"))
    }

    fun `게시물 등록됨`(
        resultActions: ResultActions, author: String, title: String, content: String
    ) {
        resultActions.apply {
            andExpectAll({ jsonPath("$.id").exists() },
                { jsonPath("$.author", { it.equals(author) }) },
                { jsonPath("$.title", { it.equals(title) }) },
                { jsonPath("$.content", { it.equals(content) }) },
                { jsonPath("$.createAt").exists() },
                { jsonPath("$.updateAt").exists() })
        }
    }

    fun `게시물 등록 및 검증`(boardRequest: BoardRequest): BoardResponse {
        //when
        val resultActions = `게시물 등록`(boardRequest)

        //then
        `게시물 등록됨`(
            resultActions = resultActions,
            author = boardRequest.author,
            title = boardRequest.title,
            content = boardRequest.content
        )

        return resultActions.andReturn().run {
            jacksonObjectMapper().readValue(response.contentAsString)
        }
    }

    @DisplayName("게시물 목록 조회")
    @Test
    fun getBoards() {
        //given
        //게시물 등록 되어 있음
        for (index in 1..10) {
            `게시물 등록 및 검증`(BoardRequest("wook $index", "title $index", "content $index"))
        }

        val resultActions = `게시물 목록 조회`()

        `게시물 목록 조회됨`(resultActions)
    }

    private fun `게시물 목록 조회됨`(resultActions: ResultActions) {
        resultActions.andExpectAll(
            { jsonPath("$.boardResponses.*.id").exists() },
            { jsonPath("$.boardResponses.*.author").exists() },
            { jsonPath("$.boardResponses.*.title").exists() },
            { jsonPath("$.boardResponses.*.content").exists() },
            { jsonPath("$.boardResponses.*.createAt").exists() },
            { jsonPath("$.boardResponses.*.updateAt").exists() }
        )
    }

    fun `게시물 목록 조회`(): ResultActions {
        return mockMvc.run {
            perform(get("/boards")).andExpect(status().isOk)
        }
    }

    @DisplayName("게시물 조회")
    @Test
    fun getBoard() {
        //given
        //게시물 등록 되어 있음
        val boardResponse = `게시물 등록 및 검증`(BoardRequest("wook", "title", "content"))

        //when
        val resultActions = `게시물 조회`(boardResponse.id)

        //then
        `게시물 조회됨`(boardResponse, resultActions)
    }

    fun `게시물 조회`(id: Long): ResultActions {
        return mockMvc.perform(get("/boards/{id}", id)).andExpect(status().isOk)
    }

    fun `게시물 조회됨`(boardResponse: BoardResponse, resultActions: ResultActions) {
        resultActions.andExpectAll(
            { jsonPath("$.id", { it.equals(boardResponse.id) }) },
            { jsonPath("$.author", { it.equals(boardResponse.author) }) },
            { jsonPath("$.title", { it.equals(boardResponse.title) }) },
            { jsonPath("$.content", { it.equals(boardResponse.content) }) },
            { jsonPath("$.createAt", { it.equals(boardResponse.createAt) }) },
            { jsonPath("$.updateAt", { it.equals(boardResponse.updateAt) }) }
        )
    }
}